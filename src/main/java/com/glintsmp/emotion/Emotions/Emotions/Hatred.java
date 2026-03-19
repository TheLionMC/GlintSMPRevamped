package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.HatredUnleashed;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Hatred extends Emotion {

    public Hatred() {
        super("hatred", new HatredUnleashed());
        addTrigger(EmotionTrigger.PLAYER_DEATH_BY_PLAYER, Type.POSITIVE, 2);
        addTrigger(EmotionTrigger.PET_KILLED, Type.POSITIVE, 3);
        addTrigger(EmotionTrigger.PLAYER_TRUST_REMOVE, Type.POSITIVE, 2);

        addTrigger(EmotionTrigger.PLAYER_TRUST_ADD, Type.NEGATIVE, 2);
        addTrigger(EmotionTrigger.GENERAL_DECREASE, Type.NEGATIVE, 1);
    }

    @Override
    protected void onIncrease(Player player, int value) {

    }

    @Override
    protected void onDecrease(Player player, int value) {

    }
}
