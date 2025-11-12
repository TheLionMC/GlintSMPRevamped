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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TrustList extends SubCommand {

    public TrustList() {
        super("list", "Get your Trustlist");
    }

    // As staff get the list of a chosen player, As normal player, get your own
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        UUID uuid = Objects.requireNonNull(player).getUniqueId();

        if (args.length < 1 || args.length > 2) {
            if (sender.hasPermission("glint.staff")) {
                sender.sendMessage(Component.text("Usage: /trust list <player>", NamedTextColor.RED));
                return true;
            } else {
                sender.sendMessage(Component.text("Usage: /trust list", NamedTextColor.RED));
                return true;
            }
        }

        if (args.length == 2 | player.hasPermission("glint.staff")) {
            Player checkplayer = Bukkit.getPlayer(args[2]);
            List<String> list = TrustManager.getTrustList(Objects.requireNonNull(checkplayer).getUniqueId());
            List<String> cleanlist = new ArrayList<>();

            for (String s : list) {
                UUID cleanuuid = UUID.fromString(s);
                Player cleanplayer = Bukkit.getPlayer(cleanuuid);
                String cleanplayername = Objects.requireNonNull(cleanplayer).getName();

                cleanlist.add(cleanplayername);
            }
            sender.sendMessage(Component.text("Trust List of:" + checkplayer.getName() + Component.newline() + cleanlist, NamedTextColor.GREEN));
        } else {
            List<String> list = TrustManager.getTrustList(uuid);
            List<String> cleanlist = new ArrayList<>();

            for (String s : list) {
                UUID cleanuuid = UUID.fromString(s);
                Player cleanplayer = Bukkit.getPlayer(cleanuuid);
                String cleanplayername = Objects.requireNonNull(cleanplayer).getName();

                cleanlist.add(cleanplayername);
            }
            sender.sendMessage(Component.text("Your trust list:" + Component.newline() + cleanlist, NamedTextColor.GREEN));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            if (sender.hasPermission("glint.staff")) return null;
            else return List.of(sender.getName());
        } else {
            return List.of();
        }
    }
}
