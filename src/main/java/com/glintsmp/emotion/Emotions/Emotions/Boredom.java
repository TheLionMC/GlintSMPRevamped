package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.Clumsiness;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Boredom extends Emotion {

    public Boredom() {
        super("boredom", new Clumsiness());

        addTrigger(EmotionTrigger.ALONE_MINUTE,Type.POSITIVE,1);

        addTrigger(EmotionTrigger.WITH_PLAYER_MINUTE,Type.NEGATIVE,1);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_TOAST, Type.NEGATIVE, 3);
        addTrigger(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_NORMAL, Type.NEGATIVE, 1);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
