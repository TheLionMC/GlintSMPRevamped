package com.glintsmp.emotion.RelationshipAlgorithm;

import com.glintsmp.emotion.Managers.RelationshipManager;

import java.util.UUID;

public class RelationshipAdjuster {

    public static void adjustPositive(UUID player, UUID target, int baseChange) {
        int current = RelationshipManager.getRelationship(player, target);
        int adjusted = RelationshipAlgorithm.calculateIncrease(current, baseChange);
        RelationshipManager.increaseRelationship(player, target, adjusted);
    }

    public static void adjustNegative(UUID player, UUID target, int baseChange) {
        int current = RelationshipManager.getRelationship(player, target);
        int adjusted = RelationshipAlgorithm.calculateDecrease(current, baseChange);
        RelationshipManager.decreaseRelationship(player, target, adjusted);
    }
}
