package com.glintsmp.emotion.Commands.Commands.Lives;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Managers.LifeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WithdrawCommand extends SubCommand {

    public WithdrawCommand() {
        super("withdraw", "Withdraw lives from your own supply");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            sender.sendMessage(Component.text("Invalid usage: /life withdraw <amount>", NamedTextColor.RED));
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Pleas enter a valid number \uD83D\uDE21"));
            return true;
        }

        int current = LifeManager.getLives(player.getUniqueId());
        if (amount >= current) {
            player.sendMessage(Component.text("You do not have enough lives to withdraw " + amount,
                    NamedTextColor.RED));
            return true;
        }

        Inventory inventory = player.getInventory();

        ItemStack itemStack = LifeManager.getItem().getItemStack(player);

        int amountWithdrawn = 0;
        for (int i = 1; i <= amount; i++) {
            if (inventory.firstEmpty() == -1) {
                player.sendMessage(Component.text("Failed to withdraw all lives, not enough inventory space.",
                        NamedTextColor.RED));
                break;
            }

            amountWithdrawn++;
            inventory.addItem(itemStack.clone());
        }

        LifeManager.decrementLives(player.getUniqueId(), amountWithdrawn);
        player.sendMessage(Component.text("Successfully withdrew " + amountWithdrawn + " lives", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("<amount>");
    }
}
