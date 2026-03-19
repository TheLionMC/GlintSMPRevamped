package com.glintsmp.emotion.Events;

import com.glintsmp.emotion.Afk.AfkStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import org.jetbrains.annotations.NotNull;

public class PlayerAFKEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();
    private final AfkStatus afkStatus;

    public PlayerAFKEvent(@NotNull Player who, AfkStatus afkStatus) {
        super(who);
        this.afkStatus = afkStatus;
    }

    public AfkStatus getAfkStatus() {
        return afkStatus;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}