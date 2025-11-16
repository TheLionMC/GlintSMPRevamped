package com.glintsmp.emotion.Emotions.Passive.Passives;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Passive.Passive;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public record PotionPassive(PotionEffect effect) implements Passive {

    @Override
    public void apply(Player player, Emotion emotion) {
        player.addPotionEffect(effect);
    }

    @Override
    public void clear(Player player) {
        player.removePotionEffect(effect.getType());
    }
}
