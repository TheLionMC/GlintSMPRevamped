package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.ConfidenceBoost;
import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

public class Confidence extends Emotion {

    public Confidence() {
        super("confidence", new ConfidenceBoost());

        setDefaultValue(60);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
