package com.glintsmp.emotion.Afk;

import com.glintsmp.emotion.Events.PlayerAFKEvent;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Utils.TickUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class AfkHandler implements Listener {

    private static final Map<Player, AfkStatus> afkStatusMap = new HashMap<>();

    public static void initialize() {
        Bukkit.getPluginManager().registerEvents(new AfkHandler(), GlintSMP.getInstance());

        GlintSMP.runTaskTimer(integer -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                AfkStatus afkStatus = afkStatusMap.get(player);
                if (afkStatus == null) continue;

                if (isAfk(player)) {
                    PlayerAFKEvent event = new PlayerAFKEvent(player, afkStatus);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }

            return true;
        },0, TickUtils.minute(3));
    }

    public static boolean isAfk(Player player) {
        AfkStatus afkStatus = afkStatusMap.get(player);
        return afkStatus != null && afkStatus.isAfk();
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        AfkStatus afkStatus = afkStatusMap.computeIfAbsent(player, p -> new AfkStatus());

        Location from = event.getFrom();
        Location location = event.getTo();
        if (from.distance(location) < .10) return;

        afkStatus.getHistory().record(new AfkStatus.MovementSample(location, System.currentTimeMillis(), location.getYaw(), location.getPitch(),
                location.clone().subtract(0,0.5,0).getBlock().isSolid()));
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        AfkStatus afkStatus = afkStatusMap.computeIfAbsent(player, p -> new AfkStatus());

        afkStatus.setLastTimeInteracted(System.currentTimeMillis());
    }
}
