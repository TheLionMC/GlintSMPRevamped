package com.glintsmp.emotion.Commands.Commands.Ghost;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Ghost.GhostManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class GhostWorldCommand extends SubCommand {

    public GhostWorldCommand() {
        super("world", "enters or leaves the ghost world", "glint.admin");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            player.sendMessage(Component.text("Invalid usage: /ghost world <enter | leave>", NamedTextColor.RED));
            return true;
        }

        boolean isInGhostWorld = GhostManager.getWorld().contains(player);

        String arg = args[1];
        if (arg.equals("enter")) {
            if (isInGhostWorld) {
                player.sendMessage(Component.text("You are already in the ghost world", NamedTextColor.RED));
                return true;
            }

            GhostManager.getWorld().addGhost(player);
            player.sendMessage(Component.text("You have entered the ghost world.", NamedTextColor.GRAY));
        } else if (arg.equals("leave")) {
            if (!isInGhostWorld) {
                player.sendMessage(Component.text("You are not in the ghost world", NamedTextColor.RED));
                return true;
            }

            GhostManager.getWorld().removeGhost(player);
        } else
            player.sendMessage(Component.text("Invalid usage: /ghost world <enter | leave>", NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (args.length == 2)
            return List.of("enter", "leave");
        return List.of();
    }
}
