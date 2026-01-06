package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative;

import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;

public class RuiningBuild extends Trigger {
    public RuiningBuild() { super("ruiningbuild"); }
    @Override
    public int change(int strength, boolean positive) {
        int mult = 3; int s = Math.max(1, strength * mult);
        if (!positive) { int dec = computeDecrease(s); if (dec>=0) return 0; return -randomness(Math.abs(dec)); }
        return randomness(computeIncrease(Math.max(1, strength/6)));
    }
}

