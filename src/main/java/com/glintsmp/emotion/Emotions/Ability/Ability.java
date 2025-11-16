package com.glintsmp.emotion.Emotions.Ability;

import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

public interface Ability {
    boolean activate(Player player, Emotion emotion);
}