package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TrustManager {

    private static YamlConfiguration config;
    private static File trustFile;

    /**
     * Check if a certain user is trusted
     **/
    public static boolean isTrusted(UUID playeruuid, UUID checkuuid) {
        List<String> trusted = config.getStringList(playeruuid.toString());
        if (trusted.isEmpty()) return false;

        return trusted.contains(checkuuid.toString());
    }

    /**
     * Add a certain player to the trusted list
     */
    public static void addTrusted(UUID playeruuid, UUID adduuid) {
        List<String> trusted = config.getStringList(playeruuid.toString());

        if (!trusted.contains(adduuid.toString()))
            trusted.add(adduuid.toString());

        config.set(playeruuid.toString(), trusted);
        save();
    }

    /**
     * Remove a certain player from the trusted list
     */
    public static void removeTrusted(UUID playeruuid, UUID removeuuid) {
        List<String> trusted = config.getStringList(playeruuid.toString());
        trusted.remove(removeuuid.toString());

        config.set(playeruuid.toString(), trusted);
        save();
    }

    /**
     * Get the trustlist of a certain player
     */
    public static List<String> getTrustList(UUID checkuuid) {
        return config.getStringList(checkuuid.toString());
    }

    public static void save() {
        try {
            config.save(trustFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // initialize data
    public static void initialize(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.mkdirs())
            GlintSMP.logger.info("Created data folder");

        File file = new File(dataFolder, "trusted.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    GlintSMP.logger.info("Created new trusted.yml file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        trustFile = file;
        config = YamlConfiguration.loadConfiguration(file);
    }
}
