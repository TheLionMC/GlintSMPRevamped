package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.HatredUnleashed;
import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

public class Hatred extends Emotion {

    public Hatred() {
        super("hatred", new HatredUnleashed());
    }

    @Override
    protected void onIncrease(Player player, int value) {

    }

    @Override
    protected void onDecrease(Player player, int value) {

    }
}
