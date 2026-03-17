package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.ShockParalysis;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Surprise extends Emotion {

    public Surprise() {
        super("surprise", new ShockParalysis());
        addTrigger(EmotionTrigger.PLAYER_WITNESS_DEATH, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_TOAST, Type.POSITIVE, 4);
        addTrigger(EmotionTrigger.PLAYER_EXPERIENCES_LAG_SPIKE, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_NORMAL, Type.POSITIVE, 2);

        addTrigger(EmotionTrigger.GENERAL_DECREASE, Type.NEGATIVE, 1);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
