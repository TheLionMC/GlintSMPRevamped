package com.glintsmp.emotion.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private CommandExecutor executor;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String @NonNull [] args) {
        if (args.length == 0) {
            if (executor == null)
                sender.sendMessage(Component.text("Invalid command usage /" + label + " <subCommand>")
                    .color(NamedTextColor.RED));
            else executor.onCommand(sender, command, label, args);
            return true;
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(args[0])) {
                if (subCommand.hasPermission(sender))
                    return subCommand.onCommand(sender, command, label, args);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> collections = new ArrayList<>();

        if (args.length == 1) {
            String current = args[0].toLowerCase();
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().toLowerCase().startsWith(current))
                    collections.add(subCommand.getName());
            }
            return collections;
        }

        if (args.length >= 2) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    if (subCommand.hasPermission(sender))
                        return subCommand.onTabComplete(sender, command, label, args);
                }
            }
        }

        return collections;
    }

    public Command setExecutor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public void registerSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }
}