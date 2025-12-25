package com.glintsmp.emotion.RelationshipAlgorithm;

import com.glintsmp.emotion.GlintSMP;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RelationshipEventHandler implements Listener {

    private static final Map<InteractionKey, InteractionWindow> windows = new ConcurrentHashMap<>();
    private static final long CLEANUP_PERIOD_PEAK = 20L * 60L * 10L;
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(10);
    private static final int ABUSE_THRESHOLD_PER_WINDOW = 30;
    private static final int PERIOD_THRESHOLD = 10; //FIXME: Only for person type 2 (if you don't know what that is you aren't meant to be in this codebase), declare this somewhere

    private final Map<String, Long> proximityTimers = new HashMap<>();
    private final Map<String, Long> giftTimers = new HashMap<>();

    public RelationshipEventHandler(Plugin plugin) {
        new BukkitRunnable() {
            @Override public void run() { cleanup(); }
        }.runTaskTimer(plugin, CLEANUP_PERIOD_PEAK, CLEANUP_PERIOD_PEAK);
    }

    private record InteractionKey(UUID a, UUID b) {
        private InteractionKey(UUID a, UUID b) {
            if (a.toString().compareTo(b.toString()) <= 0) { this.a = a; this.b = b; }
            else { this.a = b; this.b = a; }
        }
        @Override public boolean equals(Object o) {
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
            while (!events.isEmpty() && Duration.between(events.peekFirst(), now).compareTo(WINDOW_DURATION) > 0)
                events.removeFirst();
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
        return 0.97 + r.nextDouble() * 0.06;
    }

    private boolean abusing(UUID a, UUID b) {
        return windowFor(a, b).count() > ABUSE_THRESHOLD_PER_WINDOW;
    }

    private void record(UUID a, UUID b) {
        windowFor(a, b).addEvent();
    }

    private void apply(UUID src, UUID tgt, double base, boolean positive) {
        record(src, tgt);

        InteractionWindow w = windowFor(src, tgt);

        if (abusing(src, tgt)) {
            w.penalizedCount++;
            base = Math.max(1, base / (2 + w.penalizedCount));
        }

        double factor = dailyRandomFactor(src, tgt);
        int adjusted = (int) Math.round(base * factor);
        if (adjusted < 1 && base >= 1) adjusted = 1;

        if (positive) RelationshipAdjuster.adjustPositive(src, tgt, adjusted);
        else RelationshipAdjuster.adjustNegative(src, tgt, adjusted);

        int mutual = Math.max(1, adjusted / 4);
        if (positive) RelationshipAdjuster.adjustPositive(tgt, src, mutual);
        else RelationshipAdjuster.adjustNegative(tgt, src, mutual);
    }

    private Collection<Player> near(Player p, double r) {
        List<Player> list = new ArrayList<>();
        for (Entity e : p.getNearbyEntities(r, r, r)) if (e instanceof Player x) list.add(x);
        return list;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        UUID a = null, b = null;
        if (e.getDamager() instanceof Player p) a = p.getUniqueId();
        else if (e.getDamager() instanceof Projectile pro) {
            ProjectileSource s = pro.getShooter();
            if (s instanceof Player p) a = p.getUniqueId();
        }
        if (e.getEntity() instanceof Player p) b = p.getUniqueId();
        if (a == null || b == null || a.equals(b)) return;

        apply(a, b, 6, false);
        apply(b, a, 3, false);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player v = e.getEntity();
        Player k = v.getKiller();
        if (k == null) return;

        apply(k.getUniqueId(), v.getUniqueId(), 20, false);
        apply(v.getUniqueId(), k.getUniqueId(), 15, false);
    }

    @EventHandler
    public void onSharedCombat(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player k)) return;
        for (Player t : near(k, 6)) {
            if (!t.equals(k)) apply(k.getUniqueId(), t.getUniqueId(), 2, true);
        }
    }

    @EventHandler
    public void onLongProximity(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        for (Player o : Bukkit.getOnlinePlayers()) {
            if (p.equals(o)) continue;
            double d = p.getLocation().distance(o.getLocation());
            if (d > 2.5) continue;

            String key = p.getUniqueId() + ":" + o.getUniqueId();
            proximityTimers.putIfAbsent(key, System.currentTimeMillis());

            if (System.currentTimeMillis() - proximityTimers.get(key) >= 60000) {
                apply(p.getUniqueId(), o.getUniqueId(), 1, true);
                proximityTimers.put(key, System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onGift(PlayerDropItemEvent e) {
        Player d = e.getPlayer();
        ItemStack it = e.getItemDrop().getItemStack();

        for (Player o : near(d, 2.5)) {
            if (o.equals(d)) continue;

            String key = d.getUniqueId() + ":" + o.getUniqueId();
            giftTimers.put(key, System.currentTimeMillis());

            Bukkit.getScheduler().runTaskLater(GlintSMP.getInstance(), () -> {
                if (o.getInventory().containsAtLeast(it, 1)) {
                    if (System.currentTimeMillis() - giftTimers.get(key) <= 5000) {
                        apply(d.getUniqueId(), o.getUniqueId(), 0.001, true);
                    }
                }
            }, 40L);
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        for (Player o : near(p, 20)) {
            if (!o.equals(p)) apply(p.getUniqueId(), o.getUniqueId(), 1, true);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player p = e.getPlayer();
        for (Player o : near(p, 2)) apply(p.getUniqueId(), o.getUniqueId(), 1, true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (Player o : near(p, 8)) apply(o.getUniqueId(), p.getUniqueId(), 1, true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        windows.entrySet().removeIf(en -> en.getKey().a.equals(u) || en.getKey().b.equals(u));
    }
}
