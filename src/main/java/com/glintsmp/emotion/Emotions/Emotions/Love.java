package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.Heal;
import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

public class Love extends Emotion {

    public Love() {
        super("love", new Heal());
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
