package com.glintsmp.emotion.Commands.Commands.Lives;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Managers.LifeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetLivesCommand extends SubCommand {

    public SetLivesCommand() {
        super("set", "Set the lives of a specific Player.", "glint.staff");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            sender.sendMessage(Component.text("Invalid usage: /life set <player> <amount>", NamedTextColor.RED));
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(Component.text("Could not find a player named: " + args[1]));
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Pleas enter a valid number \uD83D\uDE21"));
            return true;
        }

        LifeManager.setLives(player.getUniqueId(), amount);
        sender.sendMessage(Component.text("set lives for " + player.getName() + " to " + Math.max(0, Math.min(5, amount)),
                NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2)
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList();

        if (args.length == 3)
            return List.of("<amount>");

        return List.of();
    }
}
