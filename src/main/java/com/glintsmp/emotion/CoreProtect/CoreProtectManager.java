package com.glintsmp.emotion.CoreProtect;

import com.glintsmp.emotion.Emotions.Emotions.*;
import com.glintsmp.emotion.GlintSMP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CoreProtectManager implements Listener {

    private static final Map<String, ProtectedRegion> protectedRegions = new HashMap<>();

    private static YamlConfiguration config;
    private static File file;

    public static ProtectedRegion createRegion(Location corner1, Location corner2) {
        if (!corner1.getWorld().equals(corner2.getWorld()))
            throw new IllegalArgumentException("Failed to create new protected region, make sure the selected locations are in the same world");

        return new ProtectedRegion(corner1.getWorld(), new BoundingBox(
                corner1.getX(), corner1.getY(), corner1.getZ(),
                corner2.getX(), corner2.getY(), corner2.getZ()));
    }

    public static void protect(String name, ProtectedRegion region) {
        protectedRegions.put(name, region);

        saveRegion(name, region);
    }

    public static void unProtect(String name) {
        protectedRegions.remove(name);
        config.set("regions." + name, null);

        save();
    }

    public static ProtectedRegion getProtected(String name) {
        return protectedRegions.get(name);
    }

    public static Collection<ProtectedRegion> getProtected() {
        return protectedRegions.values();
    }

    public static boolean isProtected(Location location) {
        for (ProtectedRegion protect : getProtected()) {
            if (!protect.world().equals(location.getWorld())) continue;

            if (protect.boundingBox().contains(location.toVector()))
                return true;
        }

        return false;
    }

    public static void saveRegion(String name, ProtectedRegion region) {
        String path = "regions." + name;

        config.set(path + ".world", region.world().getName());
        config.set(path + ".box", region.boundingBox().serialize());

        save();
    }

    private static void loadData() {
        protectedRegions.clear();

        ConfigurationSection regions = config.getConfigurationSection("regions");
        if (regions == null) return;

        for (String name : regions.getKeys(false)) {
            ConfigurationSection region = regions.getConfigurationSection(name);
            if (region == null) continue;

            String worldName = region.getString("world");
            if (worldName == null) continue;

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                GlintSMP.logger.warning("World not loaded for region: " + name);
                continue;
            }

            ConfigurationSection box = region.getConfigurationSection("box");
            if (box == null) {
                GlintSMP.logger.warning("Missing box for region: " + name);
                continue;
            }

            double minX = box.getDouble("minX");
            double minY = box.getDouble("minY");
            double minZ = box.getDouble("minZ");
            double maxX = box.getDouble("maxX");
            double maxY = box.getDouble("maxY");
            double maxZ = box.getDouble("maxZ");

            BoundingBox boundingBox = new BoundingBox(
                    Math.min(minX, maxX),
                    Math.min(minY, maxY),
                    Math.min(minZ, maxZ),
                    Math.max(minX, maxX),
                    Math.max(minY, maxY),
                    Math.max(minZ, maxZ)
            );

            protectedRegions.put(name, new ProtectedRegion(world, boundingBox));

            GlintSMP.logger.info("Loaded region: " + name);
        }
    }


    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initialize(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.mkdirs())
            GlintSMP.logger.info("Created data folder");

        File file = new File(dataFolder, "coreprotect.yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile())
                    GlintSMP.logger.info("Created new coreprotect.yml file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        CoreProtectManager.file = file;

        Bukkit.getPluginManager().registerEvents(new CoreProtectListener(), plugin);

        loadData();
    }
}
