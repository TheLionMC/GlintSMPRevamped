package com.glintsmp.emotion.Emotions.Passive;

import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

public interface Passive {
    void apply(Player player, Emotion emotion);
    void clear(Player player);
}
