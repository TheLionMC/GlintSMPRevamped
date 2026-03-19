package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.Heal;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Love extends Emotion {

    public Love() {
        super("love", new Heal());
        addTrigger(EmotionTrigger.WITH_PLAYER_MINUTE, Type.POSITIVE,1);
        addTrigger(EmotionTrigger.PLAYER_TRUST_ADD, Type.POSITIVE, 2);

        addTrigger(EmotionTrigger.PLAYER_TRUST_REMOVE, Type.NEGATIVE, 2);
        addTrigger(EmotionTrigger.GENERAL_DECREASE, Type.NEGATIVE, 1);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
