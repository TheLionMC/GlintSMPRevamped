package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Emotions.*;
import com.glintsmp.emotion.GlintSMP;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EmotionManager {

    public static Map<String, Emotion> emotions = new HashMap<>();

    private static YamlConfiguration config;
    private static File emotionFile;

    public static @Nullable Emotion getEmotion(String id) {
        return emotions.get(id);
    }

    public static Collection<Emotion> getEmotions() {
        return emotions.values();
    }

    public static @NotNull Emotion getHighest(Player player) {
        UUID uuid = player.getUniqueId();

        Emotion highest = getEmotions().iterator().next();
        int highestLevel = Integer.MIN_VALUE;

        for (Emotion emotion : getEmotions()) {
            int level = getEmotionLevel(emotion, uuid);

            if (level < 0) continue;

            if (level > highestLevel) {
                highestLevel = level;
                highest = emotion;
            }
        }

        return highest;
    }

    public static Emotion getRandom() {
        return getEmotions().stream().toList().get(new Random().nextInt(getEmotions().size()));
    }

    public static int getEmotionLevel(Emotion emotion, UUID uuid) {
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if (section == null)
            section = config.createSection(uuid.toString());

        if (!section.contains(emotion.getId())) {
            section.set(emotion.getId(), 0);
            save();
            return 0;
        }

        return section.getInt(emotion.getId());
    }

    public static void setEmotionLevel(Emotion emotion, UUID uuid, int newLevel) {
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if (section == null) section = config.createSection(uuid.toString());

        int oldLevel = section.getInt(emotion.getId(), 0);

        newLevel = Math.min(100, Math.max(0, newLevel));

        int diff = newLevel - oldLevel;

        if (diff != 0) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                if (diff > 0) emotion.increase(player, diff);
                else emotion.decrease(player, -diff);
            }
        }

        section.set(emotion.getId(), newLevel);
        save();
    }


    public static void increaseEmotionLevel(Emotion emotion, Player player, int amount) {
        UUID uuid = player.getUniqueId();

        int current = getEmotionLevel(emotion, uuid);
        int newLevel = current + amount;

        emotion.increase(player, amount);
        setEmotionLevel(emotion, uuid, Math.min(100, Math.max(0, newLevel)));
    }

    public static void decreaseEmotionLevel(Emotion emotion, Player player, int amount) {
        UUID uuid = player.getUniqueId();

        int current = getEmotionLevel(emotion, uuid);
        int newLevel = current - amount;

        emotion.decrease(player, amount);
        setEmotionLevel(emotion, uuid, Math.min(100, Math.max(0, newLevel)));
    }

    public static void save() {
        try {
            config.save(emotionFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialize(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.mkdirs())
            GlintSMP.logger.info("Created data folder");

        File file = new File(dataFolder, "emotions.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    GlintSMP.logger.info("Created new emotions.yml file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        emotionFile = file;
        config = YamlConfiguration.loadConfiguration(file);
        registerEmotion(new Anger());
        registerEmotion(new Boredom());
        registerEmotion(new Confidence());
        registerEmotion(new Excitement());
        registerEmotion(new Fear());
        registerEmotion(new Loneliness());
        registerEmotion(new Love());
        registerEmotion(new Sadness());
        registerEmotion(new Shock());
        registerEmotion(new Surprise());
    }

    public static void registerEmotion(Emotion emotion) {
        emotions.put(emotion.getId(), emotion);
    }
}
