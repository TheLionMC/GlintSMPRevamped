package com.glintsmp.emotion.Commands.Commands.Trust;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Managers.TrustManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrustAdd extends SubCommand {

    public TrustAdd() {
        super("add", "Add someone to your trustlist.");
    }

    // Add a player to your trustlist
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /trust add <player>", NamedTextColor.RED));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (target.getName() == null) {
            sender.sendMessage(Component.text("Player " + args[1] + " does not exist!", NamedTextColor.RED));
            return true;
        }

        UUID uuid = target.getUniqueId();

        if (TrustManager.isTrusted(player.getUniqueId(), uuid)) {
            sender.sendMessage(Component.text(target.getName() + " is already trusted.", NamedTextColor.RED));
            return true;
        }

        TrustManager.addTrusted(player.getUniqueId(), uuid);
        sender.sendMessage(Component.text("Player " + target.getName() + " added to your trust list!", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return List.of();
        return Bukkit.getOnlinePlayers().stream()
                .filter(online -> !TrustManager.isTrusted(player.getUniqueId(), online.getUniqueId())
                        && !online.equals(player))
                .map(Player::getName)
                .toList();
    }
}
