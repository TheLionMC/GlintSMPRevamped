package com.glintsmp.emotion.RelationshipAlgorithm;

import com.glintsmp.emotion.GlintSMP;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RelationshipEventHandler implements Listener {

    private static final Map<InteractionKey, InteractionWindow> windows = new ConcurrentHashMap<>();
    private static final long CLEANUP_PERIOD_TICKS = 20L * 60L * 10L;
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(10);
    private static final int ABUSE_THRESHOLD_PER_WINDOW = 20;
    private static final int EFFECTIVE_MIN_EVENTS_FOR_CHANGE = 1;
    private final Map<UUID, UUID> lastGift = new HashMap<>();
    private final Map<String, Long> lookTimers = new HashMap<>();
    private final Map<UUID, Long> proximityStart = new HashMap<>();


    public RelationshipEventHandler(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanup();
            }
        }.runTaskTimer(plugin, CLEANUP_PERIOD_TICKS, CLEANUP_PERIOD_TICKS);
    }

    private record InteractionKey(UUID a, UUID b) {
            private InteractionKey(UUID a, UUID b) {
                if (a.toString().compareTo(b.toString()) <= 0) {
                    this.a = a;
                    this.b = b;
                } else {
                    this.a = b;
                    this.b = a;
                }
            }

        @Override
        public boolean equals(Object o) {
                if (!(o instanceof InteractionKey(UUID a1, UUID b1))) return false;
            return a.equals(a1) && b.equals(b1);
            }
    }

    private static class InteractionWindow {
        final Deque<Instant> events = new ArrayDeque<>();
        int penalizedCount = 0;
        Instant lastSeen = Instant.now();
        void addEvent() {
            Instant now = Instant.now();
            events.addLast(now);
            lastSeen = now;
            while (!events.isEmpty() && Duration.between(events.peekFirst(), now).compareTo(WINDOW_DURATION) > 0) events.removeFirst();
        }
        int count() { return events.size(); }
    }

    private InteractionWindow windowFor(UUID a, UUID b) {
        InteractionKey k = new InteractionKey(a, b);
        return windows.computeIfAbsent(k, key -> new InteractionWindow());
    }

    private void cleanup() {
        Instant cutoff = Instant.now().minus(Duration.ofHours(6));
        windows.entrySet().removeIf(e -> e.getValue().lastSeen.isBefore(cutoff));
    }

    private double dailyRandomFactor(UUID a, UUID b) {
        int seed = Objects.hash(a.toString(), b.toString(), LocalDate.now());
        Random r = new Random(seed);
        return 0.9 + r.nextDouble() * 0.2;
    }

    private boolean isAbusing(UUID a, UUID b) {
        InteractionWindow w = windowFor(a, b);
        return w.count() > ABUSE_THRESHOLD_PER_WINDOW;
    }

    private void recordEvent(UUID a, UUID b) {
        InteractionWindow w = windowFor(a, b);
        w.addEvent();
    }

    private void applyChange(UUID source, UUID target, int base, boolean positive) {
        recordEvent(source, target);
        if (isAbusing(source, target)) {
            InteractionWindow w = windowFor(source, target);
            w.penalizedCount++;
            base = Math.max(1, base / (1 + (w.penalizedCount / 2)));
        }
        double factor = dailyRandomFactor(source, target);
        int adjusted = (int) Math.round(base * factor);
        if (adjusted < 1 && base >= 1) adjusted = 1;
        if (positive) RelationshipAdjuster.adjustPositive(source, target, adjusted);
        else RelationshipAdjuster.adjustNegative(source, target, adjusted);
        if (wantsMutualAdjustment()) {
            int mutual = Math.max(1, adjusted / 3);
            if (positive) RelationshipAdjuster.adjustPositive(target, source, mutual);
            else RelationshipAdjuster.adjustNegative(target, source, Math.max(1, mutual / 2));
        }
    }

    private boolean wantsMutualAdjustment() { return true; }

    @EventHandler
    public void onRevive(PlayerRespawnEvent e) {
        Player revived = e.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(revived)) continue;
            if (p.getLocation().distance(revived.getLocation()) < 5) {
                RelationshipAdjuster.adjustPositive(p.getUniqueId(), revived.getUniqueId(), 5);
            }
        }
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent e) {
        Player sleeper = e.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(sleeper)) continue;
            if (p.getLocation().distance(sleeper.getLocation()) < 5) {
                RelationshipAdjuster.adjustPositive(p.getUniqueId(), sleeper.getUniqueId(), 3);
            }
        }
    }

    @EventHandler
    public void onFollow(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(p)) continue;
            if (p.getLocation().distance(target.getLocation()) < 3) {
                proximityStart.putIfAbsent(p.getUniqueId(), System.currentTimeMillis());
                if (System.currentTimeMillis() - proximityStart.get(p.getUniqueId()) > 20000) {
                    RelationshipAdjuster.adjustPositive(p.getUniqueId(), target.getUniqueId(), 2);
                    proximityStart.put(p.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onJointBuilding(BlockPlaceEvent e) {
        Player builder = e.getPlayer();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(builder)) continue;
            if (p.getLocation().distance(builder.getLocation()) < 4) {
                RelationshipAdjuster.adjustPositive(builder.getUniqueId(), p.getUniqueId(), 2);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player mover = e.getPlayer();
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(mover)) continue;
            double dist = mover.getLocation().distance(other.getLocation());
            if (dist < 2.5) {
                String key = mover.getUniqueId() + ":" + other.getUniqueId();
                lookTimers.putIfAbsent(key, System.currentTimeMillis());
                if (System.currentTimeMillis() - lookTimers.get(key) > 15000) {
                    RelationshipAdjuster.adjustPositive(mover.getUniqueId(), other.getUniqueId(), 1);
                    lookTimers.put(key, System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryShare(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(p)) continue;
            if (other.getLocation().distance(p.getLocation()) < 3) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    RelationshipAdjuster.adjustPositive(p.getUniqueId(), other.getUniqueId(), 1);
                }
            }
        }
    }

    @EventHandler
    public void onHelpAfterDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(dead)) continue;
            if (p.getLocation().distance(dead.getLocation()) < 6)
                RelationshipAdjuster.adjustPositive(p.getUniqueId(), dead.getUniqueId(), 3);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity dam = e.getDamager();
        Entity hit = e.getEntity();
        UUID actor = null;
        UUID target = null;
        if (dam instanceof Player) actor = dam.getUniqueId();
        else if (dam instanceof Projectile) {
            ProjectileSource src = ((Projectile) dam).getShooter();
            if (src instanceof Player) actor = ((Player) src).getUniqueId();
        }
        if (hit instanceof Player) target = hit.getUniqueId();
        if (actor == null || target == null || actor.equals(target)) return;

        if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) {
            applyChange(actor, target, 3, false);
            applyChange(target, actor, 1, false);
            return;
        }

        if (e.getCause() == DamageCause.PROJECTILE) {
            applyChange(actor, target, 2, false);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        Item it = e.getItem();
        if (it.getThrower() != null) {
            UUID thrower = it.getThrower();
            if (!thrower.equals(p.getUniqueId()))
                applyChange(p.getUniqueId(), thrower, 4, true);
        }
    }

    private Collection<Player> nearbyPlayers(Player p, double radius) {
        List<Player> result = new ArrayList<>();
        for (Entity ent : p.getNearbyEntities(radius, radius, radius)) {
            if (ent instanceof Player) result.add((Player) ent);
        }
        return result;
    }

    @EventHandler
    public void onGiftDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        Player dropper = e.getPlayer();

        for (Player nearby : Bukkit.getOnlinePlayers()) {
            if (nearby.equals(dropper)) continue;
            if (nearby.getLocation().distance(dropper.getLocation()) < 3) {
                lastGift.put(dropper.getUniqueId(), nearby.getUniqueId());
                Bukkit.getScheduler().runTaskLater(GlintSMP.getInstance(), () -> {
                    if (nearby.getInventory().containsAtLeast(item, 1)) {
                        RelationshipAdjuster.adjustPositive(dropper.getUniqueId(), nearby.getUniqueId(), 4);
                    }
                }, 60L);
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(p)) continue;
            if (p.getLocation().distanceSquared(other.getLocation()) < 64 * 64) {
                applyChange(p.getUniqueId(), other.getUniqueId(), 1, true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(p)) continue;
            if (other.getLocation().distanceSquared(p.getLocation()) < 100) {
                applyChange(other.getUniqueId(), p.getUniqueId(), 1, true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            for (Player teammate : nearbyPlayers(killer, 8)) {
                if (!teammate.equals(killer)) applyChange(killer.getUniqueId(), teammate.getUniqueId(), 2, true);
            }
        }
    }

    @EventHandler void onMurder(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Player victim = event.getEntity();

            applyChange(killer.getUniqueId(), victim.getUniqueId(), 30, false);
            applyChange(victim.getUniqueId(), killer.getUniqueId(),30, false);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) return;
        for (Player other : nearbyPlayers(p, 6)) applyChange(p.getUniqueId(), other.getUniqueId(), 1, true);
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (!e.isSneaking()) return;
        Collection<Player> near = nearbyPlayers(p, 3);
        for (Player other : near) applyChange(p.getUniqueId(), other.getUniqueId(), 1, true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        windows.entrySet().removeIf(entry -> entry.getKey().a.equals(p.getUniqueId()) || entry.getKey().b.equals(p.getUniqueId()));
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().toLowerCase(Locale.ROOT);
        if (cmd.startsWith("/whisper") || cmd.startsWith("/msg") || cmd.startsWith("/tell")) {
            Player p = e.getPlayer();
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(p) && p.getLocation().distanceSquared(other.getLocation()) < 16 * 16) {
                    applyChange(p.getUniqueId(), other.getUniqueId(), 1, true);
                }
            }
        }
    }
}
