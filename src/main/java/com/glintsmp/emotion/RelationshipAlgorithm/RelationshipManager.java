package com.glintsmp.emotion.RelationshipAlgorithm;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RelationshipManager {

    private static YamlConfiguration config;
    public static File relationshipFile;

    /**
     * Initializes the relationship system.
     * Loads or creates the relationships.yml file inside the plugin data folder.
     */
    public static void initialize(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.mkdirs())
            GlintSMP.logger.info("Created data folder");

        File file = new File(dataFolder, "relationships.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    GlintSMP.logger.info("Created new relationships.yml file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        relationshipFile = file;
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves all relationship data to the relationships.yml file.
     */
    public static void save() {
        try {
            config.save(relationshipFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the relationship score between two players.
     * Automatically clamps the value between 0 and 100.
     *
     * @param player The player whose data is being modified.
     * @param target The target player whose relationship score is being set.
     * @param score  The relationship score (0–100).
     */
    public static void setRelationship(UUID player, UUID target, int score) {
        score = Math.max(0, Math.min(score, 100));
        config.set(player + "." + target, score);
        save();
    }

    /**
     * Returns the current relationship score between two players.
     *
     * @param player The player whose data is being accessed.
     * @param target The target player to check.
     * @return The current relationship score (default 0).
     */
    public static int getRelationship(UUID player, UUID target) {
        return config.getInt(player + "." + target, 0);
    }

    /**
     * Increases the relationship score between two players by a given amount.
     * Does not exceed the maximum value of 100.
     *
     * @param player The player whose data is being modified.
     * @param target The target player whose score is being increased.
     * @param amount The amount to add.
     */
    public static void increaseRelationship(UUID player, UUID target, int amount) {
        int current = getRelationship(player, target);
        setRelationship(player, target, Math.min(100, current + amount));
    }

    /**
     * Decreases the relationship score between two players by a given amount.
     * Does not fall below the minimum value of 0.
     *
     * @param player The player whose data is being modified.
     * @param target The target player whose score is being decreased.
     * @param amount The amount to subtract.
     */
    public static void decreaseRelationship(UUID player, UUID target, int amount) {
        int current = getRelationship(player, target);
        setRelationship(player, target, Math.max(0, current - amount));
    }

    /**
     * Returns all stored relationship scores for a specific player.
     * The result is a map of target player UUIDs to their relationship scores.
     *
     * @param player The player whose relationship data should be returned.
     * @return A map of (target UUID → relationship score).
     */
    public static Map<UUID, Integer> getRelationships(UUID player) {
        Map<UUID, Integer> map = new HashMap<>();
        if (config.isConfigurationSection(player.toString())) {
            for (String key : config.getConfigurationSection(player.toString()).getKeys(false)) {
                map.put(UUID.fromString(key), config.getInt(player + "." + key));
            }
        }
        return map;
    }

    /**
     * Removes the relationship entry between two players.
     *
     * @param player The player whose data is being modified.
     * @param target The target player to remove.
     */
    public static void resetRelationship(UUID player, UUID target) {
        if (config.contains(player + "." + target)) {
            config.set(player + "." + target, null);
            save();
        }
    }

    /**
     * Removes all stored relationships for a specific player.
     *
     * @param player The player whose entire relationship data should be cleared.
     */
    public static void resetAll(UUID player) {
        config.set(player.toString(), null);
        save();
    }

    /**
     * Returns the current YamlConfiguration instance.
     * Useful for other systems (e.g. RelationshipDecay) to read keys directly.
     */
    public static YamlConfiguration getConfig() {
        return config;
    }
}
