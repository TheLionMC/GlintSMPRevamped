package com.glintsmp.emotion.RelationshipAlgorithm.Triggers;

import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipAdjuster;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central listener that maps common in-game events to Trigger IDs and
 * applies the Trigger change using RelationshipAdjuster.
 *
 * This file now contains a heuristic mapping so every trigger ID from
 * TriggerRegistry is invoked from at least one in-game event. The mapping
 * is intentionally conservative and can be tuned later.
 */
public class TriggerListener implements Listener {

    private enum EventType {
        CHAT, COMBAT, DEATH, DROP_PICKUP, INTERACT, CONSUME, BUILD, SOCIAL, QUIT
    }

    private static final Map<EventType, Map<String, Boolean>> EVENT_MAP = new EnumMap<>(EventType.class);
    static {
        for (EventType t : EventType.values()) EVENT_MAP.put(t, new HashMap<>());

        EVENT_MAP.get(EventType.CONSUME).put("bonding", true);
        EVENT_MAP.get(EventType.BUILD).put("buildingtogether", true);
        EVENT_MAP.get(EventType.CHAT).put("storytelling", true);
        EVENT_MAP.get(EventType.CHAT).put("sharingknowledge", true);
        EVENT_MAP.get(EventType.CHAT).put("complimenting", true);
        EVENT_MAP.get(EventType.CHAT).put("sharingknowledge", true);
        EVENT_MAP.get(EventType.BUILD).put("farmingtogether", true);
        EVENT_MAP.get(EventType.BUILD).put("plantogether", true);
        EVENT_MAP.get(EventType.DROP_PICKUP).put("giftexchange", true);
        EVENT_MAP.get(EventType.DROP_PICKUP).put("gifting", true);
        EVENT_MAP.get(EventType.INTERACT).put("guiding", true);
        EVENT_MAP.get(EventType.INTERACT).put("mentoring", true);
        EVENT_MAP.get(EventType.INTERACT).put("teaching", true);
        EVENT_MAP.get(EventType.INTERACT).put("supporting", true);
        EVENT_MAP.get(EventType.INTERACT).put("trading", true);
        EVENT_MAP.get(EventType.SOCIAL).put("helpinghand", true);
        EVENT_MAP.get(EventType.SOCIAL).put("learningtogether", true);
        EVENT_MAP.get(EventType.SOCIAL).put("planTogether", true); // defensive — some IDs may vary
        EVENT_MAP.get(EventType.DEATH).put("sharedvictory", true);
        EVENT_MAP.get(EventType.SOCIAL).put("sharedquest", true);
        EVENT_MAP.get(EventType.COMBAT).put("protecting", true);
        EVENT_MAP.get(EventType.COMBAT).put("rescue", true);
        EVENT_MAP.get(EventType.SOCIAL).put("storytelling", true);
        EVENT_MAP.get(EventType.SOCIAL).put("supporting", true);
        EVENT_MAP.get(EventType.SOCIAL).put("teaching", true);
        EVENT_MAP.get(EventType.SOCIAL).put("teamwork", true);

        EVENT_MAP.get(EventType.COMBAT).put("fightingwitheachother", false);
        EVENT_MAP.get(EventType.COMBAT).put("insulting", false);
        EVENT_MAP.get(EventType.COMBAT).put("harassment", false);
        EVENT_MAP.get(EventType.COMBAT).put("threatening", false);
        EVENT_MAP.get(EventType.DEATH).put("killingeachother", false);
        EVENT_MAP.get(EventType.DEATH).put("backstabbing", false);
        EVENT_MAP.get(EventType.DROP_PICKUP).put("pickpocket", false);
        EVENT_MAP.get(EventType.DROP_PICKUP).put("stealing", false);
        EVENT_MAP.get(EventType.BUILD).put("ruiningbuild", false);
        EVENT_MAP.get(EventType.BUILD).put("vandalism", false);
        EVENT_MAP.get(EventType.CHAT).put("rumors", false);
        EVENT_MAP.get(EventType.SOCIAL).put("mocking", false);
        EVENT_MAP.get(EventType.SOCIAL).put("lying", false);
        EVENT_MAP.get(EventType.SOCIAL).put("rumors", false);
        EVENT_MAP.get(EventType.QUIT).put("abandoning", false);
        EVENT_MAP.get(EventType.QUIT).put("excluding", false);
        EVENT_MAP.get(EventType.DROP_PICKUP).put("scamming", false);
        EVENT_MAP.get(EventType.INTERACT).put("betrayal", false);
        EVENT_MAP.get(EventType.INTERACT).put("blockinghelp", false);
        EVENT_MAP.get(EventType.SOCIAL).put("disturbing", false);
        EVENT_MAP.get(EventType.DROP_PICKUP).put("hoarding", false);
        EVENT_MAP.get(EventType.SOCIAL).put("ignoring", false);
        EVENT_MAP.get(EventType.SOCIAL).put("cheating", false);
        EVENT_MAP.get(EventType.SOCIAL).put("corruption", false);
        EVENT_MAP.get(EventType.SOCIAL).put("denyingaid", false);
        EVENT_MAP.get(EventType.BUILD).put("sabotaging", false);
        EVENT_MAP.get(EventType.SOCIAL).put("exploiting", false);
        EVENT_MAP.get(EventType.SOCIAL).put("griefing", false);
        EVENT_MAP.get(EventType.SOCIAL).put("hoarding", false);
        EVENT_MAP.get(EventType.SOCIAL).put("poisoning", false);
    }

    private final Map<UUID, DropMeta> recentDrops = new ConcurrentHashMap<>();
    private record DropMeta(UUID dropper, long ts) {}

    private void applyTrigger(UUID src, UUID tgt, String triggerId, int strength, boolean positive) {
        if (src == null || tgt == null || src.equals(tgt)) return;
        Trigger t = TriggerRegistry.getById(triggerId);
        if (t == null) return;

        int delta = t.change(strength, positive);
        if (delta == 0) return;

        if (delta > 0) RelationshipAdjuster.adjustPositive(src, tgt, delta);
        else RelationshipAdjuster.adjustNegative(src, tgt, Math.abs(delta));
    }

    private void fireMappedTriggers(EventType type, UUID src, UUID tgt, int strength, Set<String> exclude) {
        Map<String, Boolean> map = EVENT_MAP.get(type);
        if (map == null) return;
        for (Map.Entry<String, Boolean> en : map.entrySet()) {
            String id = en.getKey();
            if (exclude != null && exclude.contains(id)) continue;
            applyTrigger(src, tgt, id, strength, en.getValue());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        Collection<Player> nearby = p.getNearbyEntities(20,20,20).stream().filter(en->en instanceof Player).map(en->(Player)en).toList();
        for (Player o : nearby) {
            if (o.equals(p)) continue;
            Set<String> applied = new HashSet<>();
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "storytelling", 1, true); applied.add("storytelling");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "sharingknowledge", 1, true); applied.add("sharingknowledge");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "complimenting", 1, true); applied.add("complimenting");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "rumors", 1, false); applied.add("rumors");

            fireMappedTriggers(EventType.CHAT, p.getUniqueId(), o.getUniqueId(), 1, applied);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        UUID a = null, b = null;
        if (e.getDamager() instanceof Player p) a = p.getUniqueId();
        else if (e.getDamager() instanceof Projectile pro) {
            ProjectileSource s = pro.getShooter();
            if (s instanceof Player p) a = p.getUniqueId();
        }
        if (e.getEntity() instanceof Player p) b = p.getUniqueId();
        if (a == null || b == null || a.equals(b)) return;

        int strength = Math.max(1, (int)Math.round(e.getFinalDamage()));

        Set<String> applied = new HashSet<>();
        applyTrigger(a, b, "fightingwitheachother", strength, false); applied.add("fightingwitheachother");
        applyTrigger(a, b, "insulting", 1, false); applied.add("insulting");
        applyTrigger(a, b, "harassment", Math.min(3, strength), false); applied.add("harassment");
        applyTrigger(a, b, "threatening", 1, false); applied.add("threatening");

        fireMappedTriggers(EventType.COMBAT, a, b, strength, applied);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();
        if (killer == null) return;

        Set<String> applied = new HashSet<>();
        applyTrigger(killer.getUniqueId(), victim.getUniqueId(), "killingeachother", 20, false); applied.add("killingeachother");
        applyTrigger(victim.getUniqueId(), killer.getUniqueId(), "killingeachother", 15, false); applied.add("killingeachother");
        applyTrigger(killer.getUniqueId(), victim.getUniqueId(), "backstabbing", 5, false); applied.add("backstabbing");

        // witnesses feel shared victory
        for (Player p : killer.getWorld().getNearbyEntities(killer.getLocation(),6,6,6).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (p.equals(killer)) continue;
            applyTrigger(killer.getUniqueId(), p.getUniqueId(), "sharedvictory", 2, true);
        }

        fireMappedTriggers(EventType.DEATH, killer.getUniqueId(), victim.getUniqueId(), 10, applied);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player d = e.getPlayer();
        Item ent = e.getItemDrop();
        ItemStack it = ent.getItemStack();

        recentDrops.put(ent.getUniqueId(), new DropMeta(d.getUniqueId(), System.currentTimeMillis()));
        Bukkit.getScheduler().runTaskLater(GlintSMP.getInstance(), () -> recentDrops.remove(ent.getUniqueId()), 200L);

        for (Player o : d.getNearbyEntities(2.5,2.5,2.5).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (o.equals(d)) continue;
            Set<String> applied = new HashSet<>();
            Bukkit.getScheduler().runTaskLater(GlintSMP.getInstance(), () -> {
                if (o.getInventory().containsAtLeast(it,1)) {
                    applyTrigger(d.getUniqueId(), o.getUniqueId(), "gifting", 1, true); applied.add("gifting");
                }
                fireMappedTriggers(EventType.DROP_PICKUP, d.getUniqueId(), o.getUniqueId(), 1, applied);
            }, 40L);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player picker)) return;
        Item ent = e.getItem();
        DropMeta meta = recentDrops.get(ent.getUniqueId());
        UUID pickerId = picker.getUniqueId();
        Set<String> applied = new HashSet<>();
        if (meta != null) {
            if (meta.dropper.equals(pickerId)) { recentDrops.remove(ent.getUniqueId()); return; }
            if (System.currentTimeMillis() - meta.ts <= 5000) {
                applyTrigger(meta.dropper, pickerId, "gifting", 1, true); applied.add("gifting");
                recentDrops.remove(ent.getUniqueId());
            }
        }

        for (Player p : picker.getNearbyEntities(3,3,3).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (p.getUniqueId().equals(pickerId)) continue;
            applyTrigger(pickerId, p.getUniqueId(), "pickpocket", 1, false); applied.add("pickpocket");
            applyTrigger(pickerId, p.getUniqueId(), "stealing", 1, false); applied.add("stealing");
        }

        fireMappedTriggers(EventType.DROP_PICKUP, pickerId, pickerId, 1, applied);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player target)) return;
        Player src = e.getPlayer();
        Set<String> applied = new HashSet<>();
        applyTrigger(src.getUniqueId(), target.getUniqueId(), "guiding", 1, true); applied.add("guiding");
        applyTrigger(src.getUniqueId(), target.getUniqueId(), "mentoring", 1, true); applied.add("mentoring");
        applyTrigger(src.getUniqueId(), target.getUniqueId(), "trading", 1, true); applied.add("trading");
        applyTrigger(src.getUniqueId(), target.getUniqueId(), "supporting", 1, true); applied.add("supporting");

        fireMappedTriggers(EventType.INTERACT, src.getUniqueId(), target.getUniqueId(), 1, applied);
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        for (Player o : p.getNearbyEntities(3,3,3).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (o.equals(p)) continue;
            Set<String> applied = new HashSet<>();
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "sharingfood", 1, true); applied.add("sharingfood");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "bonding", 1, true); applied.add("bonding");
            fireMappedTriggers(EventType.CONSUME, p.getUniqueId(), o.getUniqueId(), 1, applied);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        for (Player o : p.getNearbyEntities(6,6,6).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (o.equals(p)) continue;
            Set<String> applied = new HashSet<>();
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "buildingtogether", 2, true); applied.add("buildingtogether");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "teamwork", 1, true); applied.add("teamwork");
            fireMappedTriggers(EventType.BUILD, p.getUniqueId(), o.getUniqueId(), 2, applied);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        for (Player o : p.getNearbyEntities(6,6,6).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (o.equals(p)) continue;
            Set<String> applied = new HashSet<>();
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "ruiningbuild", 3, false); applied.add("ruiningbuild");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "vandalism", 2, false); applied.add("vandalism");
            fireMappedTriggers(EventType.BUILD, p.getUniqueId(), o.getUniqueId(), 3, applied);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for (Player o : p.getNearbyEntities(10,10,10).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (o.equals(p)) continue;
            applyTrigger(o.getUniqueId(), p.getUniqueId(), "abandoning", 2, false);
            applyTrigger(o.getUniqueId(), p.getUniqueId(), "excluding", 1, false);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        for (Player o : p.getNearbyEntities(4,4,4).stream().filter(en->en instanceof Player).map(en->(Player)en).toList()) {
            if (o.equals(p)) continue;
            Set<String> applied = new HashSet<>();
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "complimenting", 1, true); applied.add("complimenting");
            applyTrigger(p.getUniqueId(), o.getUniqueId(), "encouraging", 1, true); applied.add("encouraging");
            fireMappedTriggers(EventType.SOCIAL, p.getUniqueId(), o.getUniqueId(), 1, applied);
        }
    }
}
