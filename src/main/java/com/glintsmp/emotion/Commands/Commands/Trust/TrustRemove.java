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
import java.util.Objects;
import java.util.UUID;

public class TrustRemove extends SubCommand {

    public TrustRemove() {
        super("remove", "Remove someone from your trustlist.");
    }

    // Remove a player from your trustlist
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /trust remove <player>", NamedTextColor.RED));
            return true;
        }
        Player targetplayer = Bukkit.getPlayer(sender.getName());
        UUID targetplayeruuid = Objects.requireNonNull(targetplayer).getUniqueId();

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(Component.text("Player " + args[1] + " is not online!", NamedTextColor.RED));
            return true;
        }

        UUID adduuid = player.getUniqueId();

        TrustManager.removeTrusted(targetplayeruuid, adduuid);
        sender.sendMessage(Component.text("Player " + args[1] + " removed from your trusted list!", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
