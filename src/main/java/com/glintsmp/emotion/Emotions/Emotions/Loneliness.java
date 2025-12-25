package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Loneliness extends Emotion {

    public Loneliness() {
        super("loneliness", null);
        setDefaultValue(40);

        addTrigger(EmotionTrigger.ALONE_MINUTE,Type.POSITIVE,1);

        addTrigger(EmotionTrigger.WITH_PLAYER_MINUTE,Type.NEGATIVE,1);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
