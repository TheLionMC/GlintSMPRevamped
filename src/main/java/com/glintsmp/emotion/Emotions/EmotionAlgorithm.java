package com.glintsmp.emotion.Emotions;

import java.util.Random;

public class EmotionAlgorithm {

    private static final Random RANDOM = new Random();

    public static int getEmotionIncrease(double strength) {
        if (strength <= 0) return 0;

        // Maximum possible increase: 0.5% per strength point
        double maxPercent = 0.5 * strength;

        // Chance that ANY increase happens: 2% per strength point (25% max)
        double chance = Math.min(0.02 * strength, 0.25);

        if (RANDOM.nextDouble() > chance)
            return 0;

        double amount = RANDOM.nextDouble() * maxPercent;
        return (int) Math.round(amount);
    }

    public static int getEmotionDecrease(double strength) {
        if (strength <= 0) return 0;

        double maxPercent = 0.5 * strength;
        double chance = Math.min(0.02 * strength, 0.25);

        if (RANDOM.nextDouble() > chance)
            return 0;

        double amount = RANDOM.nextDouble() * maxPercent;
        return (int) Math.round(amount);
    }

}
