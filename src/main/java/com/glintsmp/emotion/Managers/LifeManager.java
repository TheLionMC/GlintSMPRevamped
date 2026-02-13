package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Item.Item;
import com.glintsmp.emotion.Item.ItemManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LifeManager {

    private static YamlConfiguration config;
    private static File livesFile;

    public static int getLives(UUID uuid) {
        return config.getInt(uuid.toString(), 5);
    }

    public static void setLives(UUID uuid, int lives) {
        config.set(uuid.toString(), Math.min(5, lives));
        save();
    }

    public static boolean incrementLives(UUID uuid, int amount) {
        int current = getLives(uuid);
        if (current + amount > 5)
            return false;

        setLives(uuid,current + amount);
        return true;
    }

    public static void decrementLives(UUID uuid, int amount) {
        setLives(uuid, getLives(uuid) - amount);
    }

    public static boolean isGhost(UUID uuid) {
        return getLives(uuid) <= 0;
    }

    public static Item getItem() {
        return ItemManager.getItem("life");
    }

    public static void initialize(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.mkdirs())
            GlintSMP.logger.info("Created data folder");

        livesFile = new File(dataFolder, "lifes.yml");
        if (!livesFile.exists()) {
            try {
                if (livesFile.createNewFile())
                    GlintSMP.logger.info("Created new lives.yml file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        config = YamlConfiguration.loadConfiguration(livesFile);
    }

    public static void save() {
        try {
            config.save(livesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
