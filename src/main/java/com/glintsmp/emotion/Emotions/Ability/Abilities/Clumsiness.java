package com.glintsmp.emotion.Emotions.Ability.Abilities;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Managers.TrustManager;
import com.glintsmp.emotion.Utils.ParticleUtils;
import com.glintsmp.emotion.Utils.TickUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.Random;

public class Clumsiness implements Ability {

    private BukkitTask distractTask;
    private BukkitTask driftTask;
    private BukkitTask endAbility;
    private Listener listener;
    private final Random random = new Random();

    @Override
    public boolean activate(Player player, Emotion emotion) {
        listener = new Listener() {
            @EventHandler
            public void onMine(BlockBreakEvent event) {
                Player target = event.getPlayer();
                if (isNearbyEnemy(player, target)) return;
                if (random.nextDouble() < .25) event.setCancelled(true); //chance of cancel
            }

            @EventHandler
            public void onSwing(EntityDamageByEntityEvent event) {
                if (!(event.getDamager() instanceof Player target)) return;
                if (isNearbyEnemy(player, target)) return;
                if (random.nextDouble() < .25) event.setCancelled(true); //chance of cancel
            }

            @EventHandler
            public void onPlace(EntityPlaceEvent event) {
                if (isNearbyEnemy(player, Objects.requireNonNull(event.getPlayer()))) return;
                if (random.nextDouble() < .25) event.setCancelled(true); //chance of cancel
            }

            @EventHandler
            public void onEat(PlayerItemConsumeEvent event) {
                if(isNearbyEnemy(player, Objects.requireNonNull(event.getPlayer()))) return;
                if(random.nextDouble() < .25) event.setCancelled(true); //chance of cancel
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, GlintSMP.getInstance());

        endAbility = GlintSMP.runTaskLater(this::deactivate, TickUtils.second(10));

        distractTask = GlintSMP.runTaskTimer(tick -> {
            if (!player.isOnline()) { deactivate(); return false; }

            for (Player target : player.getLocation().getNearbyPlayers(10)) {
                if (TrustManager.isTrusted(player.getUniqueId(), target.getUniqueId())) continue;
                if (random.nextDouble() < .25)
                    target.getInventory().setHeldItemSlot(random.nextInt(9));
            }

            return true;
        }, 0, TickUtils.second(3)); //how often switched

        driftTask = GlintSMP.runTaskTimer(tick -> {
            if (!player.isOnline()) { deactivate(); return false; }

            ParticleUtils.spawnCircle(player.getLocation(),
                    new Particle.DustOptions(Color.fromRGB(227, 93, 202), 1.5F), 10, 150);

            for (Player target : player.getLocation().getNearbyPlayers(10)) {
                if (TrustManager.isTrusted(player.getUniqueId(), target.getUniqueId())) continue;

                Location loc = target.getLocation();
                loc.setYaw(loc.getYaw() + random.nextFloat(-15f, 15f)); //how far moved (strength to say so)
                target.teleport(loc);
            }

            return true;
        }, 0, TickUtils.second(5)); //how often drifted

        return true;
    }

    public void deactivate() {
        if (distractTask != null) distractTask.cancel();
        if (driftTask != null) driftTask.cancel();
        if (endAbility != null) endAbility.cancel();
        HandlerList.unregisterAll(listener);
    }

    private boolean isNearbyEnemy(Player source, Player target) {
        if (TrustManager.isTrusted(source.getUniqueId(), target.getUniqueId())) return true;
        return !(target.getLocation().distanceSquared(source.getLocation()) <= 10 * 10);
    }
}