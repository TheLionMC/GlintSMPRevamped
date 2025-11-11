package com.glintsmp.emotion.Commands.Commands.Trust;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Trust.TrustManager;
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

public class TrustCheck extends SubCommand {
    public TrustCheck() {
        super("check", "Check if a certain player is trusted");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            sender.sendMessage(Component.text("Usage: /trust check <player>", NamedTextColor.RED));
            return true;
        }
        Player targetplayer = Bukkit.getPlayer(sender.getName());
        UUID targetplayeruuid = Objects.requireNonNull(targetplayer).getUniqueId();

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            sender.sendMessage(Component.text("Player " + args[2] + " is not online!", NamedTextColor.RED));
            return true;
        }
        UUID checkuuid = player.getUniqueId();

        boolean trusted = TrustManager.isTrusted(targetplayeruuid, checkuuid);

        if (trusted) {
            sender.sendMessage(Component.text("Player: " + player.getName() + "is trusted.", NamedTextColor.GREEN));
        } else {
            sender.sendMessage(Component.text("Player: " + player.getName() + "is not trusted.", NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
