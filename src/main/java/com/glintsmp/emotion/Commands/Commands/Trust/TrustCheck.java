package com.glintsmp.emotion.Commands.Commands.Trust;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Managers.TrustManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TrustCheck extends SubCommand {

    public TrustCheck() {
        super("check", "Check if a certain player is trusted");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /trust check <player>", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Component.text("Player " + args[1] + " is not online!", NamedTextColor.RED));
            return true;
        }

        UUID checkuuid = target.getUniqueId();

        if (TrustManager.isTrusted(player.getUniqueId(), checkuuid))
            sender.sendMessage(Component.text("Player: " + player.getName() + " is trusted.", NamedTextColor.GREEN));
        else
            sender.sendMessage(Component.text("Player: " + player.getName() + " is not trusted.", NamedTextColor.RED));


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
