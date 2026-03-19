package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.Ghost.GhostWorld;
import com.glintsmp.emotion.GlintSMP;
import org.bukkit.Bukkit;
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
