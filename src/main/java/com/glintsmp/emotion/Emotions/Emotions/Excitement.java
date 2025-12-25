package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

public class Excitement extends Emotion {

    public Excitement() {
        super("excitement", null);
        setDefaultValue(79);
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
