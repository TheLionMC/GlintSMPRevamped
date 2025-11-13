package com.glintsmp.emotion.Emotions.Abilities;

import org.bukkit.entity.Player;

public abstract class Ability {

    public void activate() {

    }

    protected abstract void onActivate(Player player);
}