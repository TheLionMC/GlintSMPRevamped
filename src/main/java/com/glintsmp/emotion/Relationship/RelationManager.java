package com.glintsmp.emotion.Relationship;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RelationManager {

    public static void setRelation(Player player, Player target, int relation) {
        NamespacedKey key = getKey(target);
        PersistentDataContainer container = player.getPersistentDataContainer();

        container.set(key, PersistentDataType.INTEGER, Math.clamp(relation, 0, 100));
    }

    public static void increaseRelation(Player player, Player target, int relation) {
        NamespacedKey key = getKey(target);
        PersistentDataContainer container = player.getPersistentDataContainer();

        int currentRelation = container.getOrDefault(key, PersistentDataType.INTEGER,0);
        container.set(key, PersistentDataType.INTEGER, Math.min(100, currentRelation + relation));
    }

    public static void decreaseRelation(Player player, Player target, int relation) {
        NamespacedKey key = getKey(target);
        PersistentDataContainer container = player.getPersistentDataContainer();

        int currentRelation = container.getOrDefault(key, PersistentDataType.INTEGER,0);
        container.set(key, PersistentDataType.INTEGER, Math.max(0, currentRelation - relation));
    }

    public static int getRelation(Player player, Player target) {
        NamespacedKey key = getKey(target);
        PersistentDataContainer container = player.getPersistentDataContainer();

        return container.getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    private static NamespacedKey getKey(Player target) {
        return new NamespacedKey(GlintSMP.getInstance(), "relationship_" + target.getUniqueId());
    }
}
