package com.glintsmp.emotion.Listeners;

import com.glintsmp.emotion.Events.PlayerAFKEvent;
import com.glintsmp.emotion.Ghost.GhostManager;
import com.glintsmp.emotion.Managers.LifeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (LifeManager.isGhost(player.getUniqueId()))
            GhostManager.addGhost(player);
    }

    @EventHandler
    public void afk(PlayerAFKEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("You are afk now waky waky.");
    }
}
