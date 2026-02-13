package com.glintsmp.emotion.Ghost;

import com.glintsmp.emotion.GlintSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GhostManager {

    private static final GhostWorld ghostWorld = new GhostWorld();

    public static void addGhost(Player player) {
        ghostWorld.addGhost(player);
    }

    public static void revive(Player player) {
    }

    public static GhostWorld getWorld() {
        return ghostWorld;
    }

    public static void initialize() {
        Bukkit.getPluginManager().registerEvents(ghostWorld, GlintSMP.getInstance());
    }
}
