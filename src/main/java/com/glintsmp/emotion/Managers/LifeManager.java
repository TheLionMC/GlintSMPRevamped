package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LifeManager {

    private static YamlConfiguration config;
    private static File livesFile;

    public static int getLives(UUID uuid) {
        return config.getInt(uuid.toString(), 5);
    }

    public static void setLives(UUID uuid, int lives) {
        config.set(uuid.toString(), lives);
        save();
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

    public static ItemStack getLifeItem() {
        return ItemBuilder.of(Material.POTION)
                .displayName(Component.text("Life", NamedTextColor.RED))
                .lore(List.of(Component.text("The energy of life itself", NamedTextColor.WHITE), Component.text("contained in a simple bottle.", NamedTextColor.WHITE), Component.text("Ancient. Waiting to be used.", NamedTextColor.WHITE)))
                .setPotionColor(Color.RED)
                .build();
    }

    public static void setGhostState(UUID targetUUID) {
        
    }
}
