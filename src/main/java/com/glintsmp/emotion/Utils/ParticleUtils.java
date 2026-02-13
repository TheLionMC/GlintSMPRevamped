package com.glintsmp.emotion.Utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleUtils {

    public static void spawnCircle(Location center, Particle particle, double radius, long count) {
        World world = center.getWorld();
        if (world == null) return;

        double increment = (2 * Math.PI) / count;

        for (int i = 0; i < count; i++) {
            double angle = i * increment;

            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);

            Location point = new Location(world, x, center.getY(), z);
            world.spawnParticle(particle, point, 1);
        }
    }

    public static void spawnCircle(Location center, Particle particle, double radius, long count, double offsetX, double offsetY, double offsetZ, double extra) {
        World world = center.getWorld();
        if (world == null) return;

        double increment = (2 * Math.PI) / count;

        for (int i = 0; i < count; i++) {
            double angle = i * increment;

            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);

            Location point = new Location(world, x, center.getY(), z);
            world.spawnParticle(particle, point, 1, offsetX, offsetY, offsetZ, extra);
        }
    }


    public static void spawnCircle(Location center, Particle.DustOptions options, double radius, long count) {
        World world = center.getWorld();
        if (world == null) return;

        double increment = (2 * Math.PI) / count;

        for (int i = 0; i < count; i++) {
            double angle = i * increment;

            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);

            Location point = new Location(world, x, center.getY(), z);
            world.spawnParticle(Particle.DUST, point, 1, options);
        }
    }


}
