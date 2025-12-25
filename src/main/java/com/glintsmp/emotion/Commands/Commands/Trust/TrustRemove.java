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
import java.util.Objects;
import java.util.UUID;

public class TrustRemove extends SubCommand {

    public TrustRemove() {
        super("remove", "Remove someone from your trust list.");
    }

    // Remove a player from your trust list
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /trust remove <player>", NamedTextColor.RED));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (target.getName() == null) {
            sender.sendMessage(Component.text("Player " + args[1] + " does not exist!", NamedTextColor.RED));
            return true;
        }

        UUID uuid = target.getUniqueId();

        if (!TrustManager.isTrusted(player.getUniqueId(), uuid)) {
            sender.sendMessage(Component.text(target.getName() + " was never in your trust list.", NamedTextColor.RED));
            return true;
        }

        TrustManager.removeTrusted(player.getUniqueId(), uuid);
        sender.sendMessage(Component.text("Player " + args[1] + " removed from your trusted list!", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return List.of();

        List<String> collections = new ArrayList<>();

        for (String uuid : TrustManager.getTrustList(player.getUniqueId())) {
            OfflinePlayer trusted = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            if (trusted.getName() == null) continue;

            collections.add(trusted.getName());
        }

        return collections;
    }
}
