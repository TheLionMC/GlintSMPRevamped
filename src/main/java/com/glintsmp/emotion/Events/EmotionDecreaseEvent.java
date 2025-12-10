package com.glintsmp.emotion.Events;

import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EmotionDecreaseEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();

    private final Emotion emotion;
    private final int decreased;

    public EmotionDecreaseEvent(@NotNull Player who, Emotion emotion, int decreased) {
        super(who);
        this.emotion = emotion;
        this.decreased = decreased;
    }

    public int getDecreased() {
        return decreased;
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
