package com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive;

import com.glintsmp.emotion.Managers.RelationshipManager;
import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Trigger;
import org.bukkit.entity.Player;

public class FightingWithEachOther extends Trigger {

    public FightingWithEachOther() {
        super("fightingwitheachother");
    }

    @Override
    public int change(int strength, boolean positive) {
        return 0;
    }
}
