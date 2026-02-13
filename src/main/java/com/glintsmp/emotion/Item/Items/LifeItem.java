package com.glintsmp.emotion.Item.Items;

import com.glintsmp.emotion.Item.Consumable;
import com.glintsmp.emotion.Item.Item;
import com.glintsmp.emotion.Managers.LifeManager;
import com.glintsmp.emotion.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import java.util.List;

public class LifeItem extends Item implements Consumable {

    public LifeItem() {
        super("life", player ->
                ItemBuilder.of(Material.POTION)
                        .displayName(Component.text("Life", NamedTextColor.RED))
                        .lore(List.of(
                                Component.text("A crystallized essence of vitality.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                                Component.text("Warm to the touch. Still alive.", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                                Component.empty(),
                                Component.text("Bound to " + player.getName() + "'s life force.", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false),
                                Component.text("To be consumed when fate demands it.", NamedTextColor.DARK_RED).decoration(TextDecoration.ITALIC, false)
                        ))
                        .addItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                        .setPotionColor(Color.RED)
                        .build());
    }

    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!LifeManager.incrementLives(player.getUniqueId(), 1)) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You are already at max lives.", NamedTextColor.RED));
            return;
        }

        player.sendMessage(Component.text("you have gained 1 life", TextColor.color(232, 49, 79)));
    }

    @Override
    protected boolean onActivate(Player player) {
        return false;
    }
}
