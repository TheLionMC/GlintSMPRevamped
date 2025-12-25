package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative;

import com.glintsmp.emotion.Managers.RelationshipManager;
import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;
import org.bukkit.entity.Player;

public class KillingEachOther extends Trigger {

    public KillingEachOther() {
        super("killingeachother");
    }

    @Override
    public int change(int strength, boolean positive) {
        return 0;
    }
}
