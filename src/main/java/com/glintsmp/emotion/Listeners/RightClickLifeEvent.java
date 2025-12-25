package com.glintsmp.emotion.Listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

import com.glintsmp.emotion.Managers.LifeManager;

public class RightClickLifeEvent implements Listener {

    @EventHandler
    public void onRightClickLifeEvent(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = e.getItem();
        if (item == null) return;

        if (!item.isSimilar(LifeManager.getLifeItem())) return;

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (item.getAmount() <= 1) {
            player.getInventory().remove(item);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        int current = LifeManager.getLives(uuid);
        LifeManager.setLives(uuid, Math.max(0, current + 1));

        player.sendMessage(Component.text("Your lives have been increased to " + LifeManager.getLives(uuid) + ".", NamedTextColor.GREEN));
    }
}
