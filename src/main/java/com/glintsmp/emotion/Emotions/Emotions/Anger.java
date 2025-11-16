package com.glintsmp.emotion.Emotions.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Abilities.Shockwave;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Passive.Passives.PotionPassive;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Anger extends Emotion {

    public Anger() {
        super("anger", new Shockwave());

        addPassive(40, new PotionPassive(new PotionEffect(PotionEffectType.STRENGTH, PotionEffect.INFINITE_DURATION,0)));
    }

    @Override
    public void onIncrease(Player player, int value) {

    }

    @Override
    public void onDecrease(Player player, int value) {

    }
}
