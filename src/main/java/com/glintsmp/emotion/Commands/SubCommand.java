package com.glintsmp.emotion.Commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {

    private final String name;
    private final String description;

    private String permission;

    public SubCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SubCommand(String name, String description, String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasPermission(CommandSender sender) {
        if (permission == null) return true;
        return sender.hasPermission(permission);
    }

    public String permission() {
        return permission;
    }

    public abstract boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args);
    public abstract @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args);
}