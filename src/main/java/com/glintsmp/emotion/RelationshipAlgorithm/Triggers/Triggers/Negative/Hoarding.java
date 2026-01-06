package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative;

import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;

public class Hoarding extends Trigger {
    public Hoarding() { super("hoarding"); }
    @Override
    public int change(int strength, boolean positive) {
        int mult = 2; int s = Math.max(1, strength * mult);
        if (!positive) { int dec = computeDecrease(s); if (dec>=0) return 0; return -randomness(Math.abs(dec)); }
        return randomness(computeIncrease(Math.max(1, strength/4)));
    }
}

