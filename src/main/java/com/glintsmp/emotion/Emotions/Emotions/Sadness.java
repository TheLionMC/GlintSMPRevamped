package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Sadness extends Emotion {

    public Sadness() {
        super("sadness", null);
        addTrigger(EmotionTrigger.ALONE_MINUTE,Type.POSITIVE,1);
        addTrigger(EmotionTrigger.PLAYER_DEATH_BY_PLAYER, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_DEATH_EXPLOSION, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_DAMAGED_BY_MOB, Type.POSITIVE, 1);

        addTrigger(EmotionTrigger.WITH_PLAYER_MINUTE, Type.NEGATIVE,1);
        addTrigger(EmotionTrigger.PLAYER_WINS_EVENT, Type.NEGATIVE, 1);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
