package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive;

import com.glintsmp.emotion.Managers.RelationshipManager;
import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;
import org.bukkit.entity.Player;

public class Teaching extends Trigger {

    public Teaching() {
        super("teaching");
    }

    @Override
    public int change(int strength, boolean positive) {
        int mult = 2;
        int s = Math.max(1, strength * mult);
        if (positive) {
            int inc = computeIncrease(s);
            return randomness(inc);
        } else {
            int dec = computeDecrease(s);
            if (dec >= 0) return 0;
            return -randomness(Math.abs(dec));
        }
    }
}

