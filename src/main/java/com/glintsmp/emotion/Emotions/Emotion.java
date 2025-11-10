package com.glintsmp.emotion.Emotions;

import org.bukkit.entity.Player;

public abstract class Emotion {

    private final String id;

    public Emotion(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void onIncrease(Player player, int value);

    public abstract void onDecrease(Player player, int value);
}
