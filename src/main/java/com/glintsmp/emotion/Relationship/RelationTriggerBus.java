package com.glintsmp.emotion.Relationship;

import org.bukkit.entity.Player;

public class RelationTriggerBus {

    public static void increase(Player player, Player target) {
        RelationManager.increaseRelation(player, target, 1);
    }

    public static void decrease(Player player, Player target) {
        RelationManager.decreaseRelation(player, target, 1);
    }

    public static void increase(Player player, Player target, int relation) {
        RelationManager.increaseRelation(player, target, relation);
    }

    public static void decrease(Player player, Player target, int relation) {
        RelationManager.decreaseRelation(player, target, relation);
    }
}
