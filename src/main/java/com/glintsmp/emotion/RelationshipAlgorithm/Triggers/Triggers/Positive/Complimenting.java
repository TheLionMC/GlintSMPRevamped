package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive;

import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;

public class Complimenting extends Trigger {
    public Complimenting() { super("complimenting"); }
    @Override
    public int change(int strength, boolean positive) {
        int mult = 1; int s = Math.max(1, strength * mult);
        if (positive) return randomness(computeIncrease(s));
        int dec = computeDecrease(s); if (dec >= 0) return 0; return -randomness(Math.abs(dec));
    }
}
