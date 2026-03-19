package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.Heal;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Passive.Passives.Love.LoveCalmAnger;
import com.glintsmp.emotion.Emotions.Passive.Passives.Love.LoveRegenPassive;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Love extends Emotion {

    public Love() {
        super("love", new Heal());

        addPassive(40, new LoveRegenPassive());
        addPassive(60, new LoveCalmAnger());

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
