package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.ConfidenceBoost;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Confidence extends Emotion {

    public Confidence() {
        super("confidence", new ConfidenceBoost());

        addTrigger(EmotionTrigger.PLAYER_WINS_EVENT, Type.POSITIVE, 4);
        addTrigger(EmotionTrigger.PLAYER_KILLED_PLAYER, Type.POSITIVE, 2);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_TOAST, Type.POSITIVE, 2);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_NORMAL, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_DEFEATS_BOSS, Type.POSITIVE, 5);

        addTrigger(EmotionTrigger.PLAYER_LOST_EVENT, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_DEATH_BY_PLAYER, Type.NEGATIVE, 4);
        addTrigger(EmotionTrigger.GENERAL_DECREASE, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.NEAR_WARDEN, Type.NEGATIVE, 1);

        setDefaultValue(60);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
