package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Fear extends Emotion {

    public Fear() {
        super("fear", null);

        addTrigger(EmotionTrigger.PLAYER_DEATH_BY_PLAYER, Type.POSITIVE, 2);

        addTrigger(EmotionTrigger.PLAYER_WINS_EVENT, Type.NEGATIVE, 5);
        addTrigger(EmotionTrigger.GENERAL_DECREASE, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_TOAST, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_NORMAL, Type.NEGATIVE, 1);

        setDefaultValue(30);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
