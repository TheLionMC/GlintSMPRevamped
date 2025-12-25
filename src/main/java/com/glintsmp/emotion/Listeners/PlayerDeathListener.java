package com.glintsmp.emotion.Listeners;

import com.glintsmp.emotion.Managers.LifeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        UUID uuid = player.getUniqueId();

        int lives = LifeManager.getLives(uuid);
        int newLives = Math.max(0, lives - 1);
        LifeManager.setLives(uuid, newLives);

        e.getDrops().add(LifeManager.getLifeItem());

        player.sendMessage(Component.text("One life has faded", NamedTextColor.RED));
    }
}
