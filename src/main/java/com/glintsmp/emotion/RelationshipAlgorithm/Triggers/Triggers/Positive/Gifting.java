package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive;

import com.glintsmp.emotion.Managers.RelationshipManager;
import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;
import org.bukkit.entity.Player;

public class Gifting extends Trigger {

    public Gifting() {
        super("gifting");
    }

    @Override
    public int change(int strength, boolean positive) {
        return 0;
    }
}
