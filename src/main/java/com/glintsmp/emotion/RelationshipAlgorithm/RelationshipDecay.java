package com.glintsmp.emotion.RelationshipAlgorithm;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class RelationshipDecay {

    public static void start(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (String key : RelationshipManager.getConfig().getKeys(false)) {
                UUID player = UUID.fromString(key);
                for (UUID target : RelationshipManager.getRelationships(player).keySet()) {
                    int current = RelationshipManager.getRelationship(player, target);
                    if (current > 0) RelationshipManager.decreaseRelationship(player, target, 1);
                }
            }
        }, 0L, 20L * 60L * 60L);
    }
}
