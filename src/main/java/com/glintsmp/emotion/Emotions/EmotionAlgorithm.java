package com.glintsmp.emotion.Emotions;

import java.util.Random;

public class EmotionAlgorithm {

    private static final Random RANDOM = new Random();

    public static int getEmotionIncrease(double strength) {
        if (strength <= 0) return 0;

        double maxPercent = 0.5 * strength;
        double chance = Math.min(0.02 * strength, 0.25);

        if (RANDOM.nextDouble() > chance)
            return 0;

        return (int) Math.floor(RANDOM.nextDouble() * maxPercent) + 1;
    }

    public static int getEmotionDecrease(double strength) {
        if (strength <= 0) return 0;

        double maxPercent = 0.5 * strength;
        double chance = Math.min(0.02 * strength, 0.25);

        if (RANDOM.nextDouble() > chance)
            return 0;

        return (int) Math.floor(RANDOM.nextDouble() * maxPercent) + 1;
    }

}
