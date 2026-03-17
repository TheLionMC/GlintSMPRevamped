package com.glintsmp.emotion.Listeners;

import com.glintsmp.emotion.Events.LoseLifeEvent;
import com.glintsmp.emotion.Ghost.GhostManager;
import com.glintsmp.emotion.Managers.LifeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        LoseLifeEvent customEvent = new LoseLifeEvent(player,1);
        Bukkit.getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) return;

        LifeManager.decrementLives(uuid, 1);

        event.getDrops().add(LifeManager.getItem().getItemStack(player));
        player.sendMessage(Component.text("One life has faded", NamedTextColor.RED));

        if (LifeManager.isGhost(player.getUniqueId())) {
            event.setCancelled(true);

            GhostManager.addGhost(player);
        }
    }
}
