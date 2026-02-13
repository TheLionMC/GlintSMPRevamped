package com.glintsmp.emotion.Emotions;

import java.util.concurrent.ThreadLocalRandom;

public class EmotionAlgorithm {

    public static int getEmotionIncrease(double strength) {
        if (strength <= 0) return 0;

        double maxPercent = 0.5 * strength;
        double chance = Math.min(0.02 * strength, 0.25);

        if (ThreadLocalRandom.current().nextDouble() > chance)
            return 0;

        return (int) Math.floor(ThreadLocalRandom.current().nextDouble() * maxPercent) + 1;
    }

    public static int getEmotionDecrease(double strength) {
        if (strength <= 0) return 0;

        double maxPercent = 0.5 * strength;
        double chance = Math.min(0.02 * strength, 0.25);

        if (ThreadLocalRandom.current().nextDouble() > chance)
            return 0;

        return (int) Math.floor(ThreadLocalRandom.current().nextDouble() * maxPercent) + 1;
    }
}