package com.glintsmp.emotion.RelationshipAlgorithm.Triggers;

import java.util.Random;

public abstract class Trigger {

    private final String id;

    public Trigger(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private static final Random RANDOM = new Random();

    public int randomness(int val) {
        if (val <= 0) return 0;
        int variance = Math.max(1, (int) Math.round(Math.abs(val) * 0.2));
        int delta = RANDOM.nextInt(variance * 2 + 1) - variance;
        return val + delta;
    }

    public abstract int change(int strength, boolean positive);

    //strength represents the severity of the change
    protected int computeIncrease(int strength) {
        return getRelationshipIncrease(strength);
    }

    //strength represents the severity of the change
    protected int computeDecrease(int strength) {
        return getRelationshipDecrease(strength);
    }

    public static int getRelationshipIncrease(int strength) {
        if (strength <= 0) return 0;

        // Maximum possible increase for this level (percentage points)
        // Level 1 -> 0.5  | Level 2 -> 1 | Level 3 -> 1.5 | etc
        double maxPercent = 0.5 * strength;

        // Chance that ANY increase happens
        // Very low at low levels, scales slowly
        double chance = Math.min(0.02 * strength, 0.25);
        // Level 1: 2%
        // Level 2: 4%
        // Level 5: 10%
        // Hard cap at 25%

        if (RANDOM.nextDouble() > chance)
            return 0;

        // Apply random percentage
        double amount = RANDOM.nextDouble() * maxPercent;

        // Return as int (0–100 scale)
        return (int) Math.round(amount);
    }

    public static int getRelationshipDecrease(int level) {
        if (level <= 0) return 0;

        double maxPercent = 0.5 * level;
        double chance = Math.min(0.02 * level, 0.25);

        if (RANDOM.nextDouble() > chance)
            return 0;

        double amount = RANDOM.nextDouble() * maxPercent;
        return -(int) Math.round(amount);
    }
}
