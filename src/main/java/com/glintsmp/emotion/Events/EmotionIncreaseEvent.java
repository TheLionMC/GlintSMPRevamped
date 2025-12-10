package com.glintsmp.emotion.Events;

import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EmotionIncreaseEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final Emotion emotion;
    private final int increased;

    public EmotionIncreaseEvent(@NotNull Player who, Emotion emotion, int increased) {
        super(who);
        this.emotion = emotion;
        this.increased = increased;
    }

    public int getIncreased() {
        return increased;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
