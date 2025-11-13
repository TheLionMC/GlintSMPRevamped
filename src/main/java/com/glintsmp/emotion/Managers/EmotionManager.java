package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Emotions.*;
import com.glintsmp.emotion.GlintSMP;
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

    /**
     * Get the emotion level of a player using playerUUID
     * returns -1 if the data does not exist
     **/
    public static @NotNull Emotion getHighest(Player player) {
        Emotion highest = getRandom();

        for (Emotion emotion : getEmotions()) {
            int level = getEmotionLevel(emotion, player.getUniqueId());
            if (level == -1 || level >= getEmotionLevel(emotion, player.getUniqueId())) continue;

            highest = emotion;
        }

        return highest;
    }

    public static Emotion getRandom() {
        return getEmotions().stream().toList().get(new Random().nextInt(getEmotions().size()));
    }

    public static int getEmotionLevel(Emotion emotion, UUID uuid) {
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if (section == null) return -1;

        return section.getInt(emotion.getId(), -1);
    }

    public static boolean setEmotionLevel(Emotion emotion, UUID uuid, int level) {
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if (section == null) {
            section = config.createSection(uuid.toString());
        }

        section.set(emotion.getId(), level);
        save();
        return true;
    }

    public static void increaseEmotionLevel(Emotion emotion, Player player, int amount) {
        UUID uuid = player.getUniqueId();

        int level = getEmotionLevel(emotion, uuid);
        if (level == -1) return;

        emotion.onDecrease(player, amount);
        setEmotionLevel(emotion, uuid, getEmotionLevel(emotion, uuid) + amount);
    }

    public static void decreaseEmotionLevel(Emotion emotion, Player player, int amount) {
        UUID uuid = player.getUniqueId();

        int level = getEmotionLevel(emotion, uuid);
        if (level == -1) return;

        emotion.onDecrease(player, amount);
        setEmotionLevel(emotion, uuid, getEmotionLevel(emotion, uuid) - amount);
    }

    public static void save() {
        try {
            config.save(emotionFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // initialize data
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
