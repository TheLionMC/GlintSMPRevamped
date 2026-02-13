package com.glintsmp.emotion.Commands.Commands.Item;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Item.Item;
import com.glintsmp.emotion.Item.ItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;

public class GiveItemCommand extends SubCommand {

    public GiveItemCommand() {
        super("give", "gives the selected player (or the commands sender) the selected item", "glintsmp.staff");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Invalid usage: /item give <item> (player)", NamedTextColor.RED));
            return true;
        }

        Item item = ItemManager.getItem(args[1]);
        if (item == null) {
            sender.sendMessage(Component.text("Could not find a item with the id: " + args[1], NamedTextColor.RED));
            return true;
        }

        if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage(Component.text("Could not find a player with the name: " + args[2], NamedTextColor.RED));
                return true;
            }

            Inventory inventory = player.getInventory();
            if (inventory.firstEmpty() == -1) {
                sender.sendMessage(Component.text(player.getName() + "'s inventory is full", NamedTextColor.RED));

                player.sendMessage(Component.text("An admin is trying to give you a custom item, pleas make sure you have a free inventory slot", NamedTextColor.RED));
                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
                return true;
            }

            inventory.addItem(item.getItemStack(player));
            sender.sendMessage(Component.text("Granted " + player.getName() + " the custom item", NamedTextColor.GREEN));

            player.sendMessage(Component.text("An admin granted you a custom item", NamedTextColor.GREEN));
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL,1,1);
            return true;
        }

        if (!(sender instanceof Player player))
            return true;

        Inventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            player.sendMessage(Component.text("Your inventory is full", NamedTextColor.RED));
            return true;
        }

        inventory.addItem(item.getItemStack(player));
        sender.sendMessage(Component.text("Granted you the custom item", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (args.length == 2)
            return ItemManager.getIds();

        if (args.length == 3) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .toList();
        }

        return Collections.emptyList();
    }
}
