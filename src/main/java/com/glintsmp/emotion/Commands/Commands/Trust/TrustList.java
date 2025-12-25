package com.glintsmp.emotion.Commands.Commands.Trust;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.GlintSMP;
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
            if (sender.hasPermission("glint.staff")) sender.sendMessage(Component.text("Usage: /trust list <player>", NamedTextColor.RED));
            else sender.sendMessage(Component.text("Usage: /trust list", NamedTextColor.RED));

            return true;
        }

        List<String> rawList;
        String title;

        if (args.length == 2 && player.hasPermission("glint.staff")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found.", NamedTextColor.RED));
                return true;
            }

            rawList = TrustManager.getTrustList(target.getUniqueId());
            title = "Trust List of " + target.getName();
        } else {
            rawList = TrustManager.getTrustList(uuid);
            title = "Your Trust List";
        }

        List<String> names = resolveNames(rawList);

        Component message = Component.text()
                .append(Component.text(title, NamedTextColor.GOLD))
                .append(Component.newline())
                .append(Component.text("────────────────", NamedTextColor.DARK_GRAY))
                .append(Component.newline())
                .append(
                        names.isEmpty()
                                ? Component.text("No trusted players.", NamedTextColor.GRAY)
                                : Component.join(
                                Component.text(", ", NamedTextColor.DARK_GRAY),
                                names.stream()
                                        .map(n -> Component.text(n, NamedTextColor.GREEN))
                                        .toList()
                        )
                )
                .build();

        sender.sendMessage(message);

        return true;
    }

    private List<String> resolveNames(List<String> uuids) {
        List<String> names = new ArrayList<>();

        for (String s : uuids) {
            try {
                UUID uuid = UUID.fromString(s);
                OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);

                if (off.getName() != null)
                    names.add(off.getName());
            } catch (IllegalArgumentException ignored) {}
        }

        return names;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3)
            if (sender.hasPermission("glint.staff")) return null;

        return List.of();
    }
}
