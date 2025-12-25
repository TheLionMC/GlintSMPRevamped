package com.glintsmp.emotion.Commands.Commands.Lives;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Managers.LifeManager;
import com.glintsmp.emotion.Managers.TrustManager;
import com.glintsmp.emotion.Utils.CheckUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

        if (args.length != 2) {
            sender.sendMessage(Component.text("Invalid input /lives withdraw <amount>", NamedTextColor.RED));
            return true;
        }

        int val = CheckUtils.isInt(args[1]);

        if (val > 5 || val < 1) {
            sender.sendMessage(Component.text("You did not provide a valid number (1-5)", NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        int currentLives = LifeManager.getLives(player.getUniqueId());

        if (val > currentLives) {
            sender.sendMessage(Component.text("You do not have the amount of lives required", NamedTextColor.RED));
            return true;
        }

        LifeManager.setLives(player.getUniqueId(), currentLives - val);

        ItemStack lifeItem = LifeManager.getLifeItem();

        lifeItem.setAmount(val);

        if (player.getInventory().firstEmpty() == -1) {
            sender.sendMessage(Component.text("You do not have the inventory space required", NamedTextColor.RED));
            return true;
        }

        player.getInventory().addItem(lifeItem);
        sender.sendMessage(Component.text("You successfully withdrew " + args[1] + "lives", NamedTextColor.GREEN));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("<amount>");
    }
}
