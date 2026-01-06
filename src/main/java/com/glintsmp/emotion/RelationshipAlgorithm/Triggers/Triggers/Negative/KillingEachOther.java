package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative;

import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;

public class KillingEachOther extends Trigger {

    public KillingEachOther() {
        super("killingeachother");
    }

    @Override
    public int change(int strength, boolean positive) {
        // very strong negative effect
        int mult = 8;
        int s = Math.max(1, strength * mult);
        if (!positive) {
            int dec = computeDecrease(s);
            if (dec >= 0) return 0;
            return -randomness(Math.abs(dec));
        } else {
            // tiny positive chance if reconciled
            int inc = computeIncrease(Math.max(1, strength / 2));
            return randomness(inc);
        }
    }
}
