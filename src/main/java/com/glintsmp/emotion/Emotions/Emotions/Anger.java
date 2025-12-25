package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.Shockwave;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import org.bukkit.entity.Player;

public class Anger extends Emotion {

    public Anger() {
        super("anger", new Shockwave());

        addTrigger(EmotionTrigger.PLAYER_DEATH_BY_PLAYER, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_KILLED_PLAYER, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_DAMAGED_BY_PLAYER, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_TOOL_BREAK, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_LOST_EVENT, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_ITEM_STOLEN, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PET_KILLED, Type.POSITIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_EXPERIENCES_LAG_SPIKE, Type.POSITIVE, 1);

        addTrigger(EmotionTrigger.PLAYER_WINS_EVENT, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_EATS_FOOD, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_SLEEPS, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_REGEN_HEALTH, Type.NEGATIVE, 1);
        addTrigger(EmotionTrigger.PLAYER_LISTENS_TO_MUSIC_DISC, Type.NEGATIVE, 1);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
