package com.glintsmp.emotion.Listeners;

import com.glintsmp.emotion.Events.EmotionDecreaseEvent;
import com.glintsmp.emotion.Events.EmotionIncreaseEvent;
import com.glintsmp.emotion.Events.PlayerGhostEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This is mainly here for testing purposes.
 * I created some custom events like {@link @EmotionIncreaseEvent}
 * so you can use this to test those out here :D
 * **/
public class CustomEvents implements Listener {

    @EventHandler
    public void onIncrease(EmotionIncreaseEvent event) {
        event.getPlayer().sendMessage(Component.text("Emotion: " + event.getEmotion().getId() + " increased by " + event.getIncreased(),
                NamedTextColor.GREEN));
    }

    @EventHandler
    public void onDecrease(EmotionDecreaseEvent event) {
        event.getPlayer().sendMessage(Component.text("Emotion: " + event.getEmotion().getId() + " decreased by " + event.getDecreased(),
                NamedTextColor.GREEN));
    }

    @EventHandler
    public void ghost(PlayerGhostEvent event) {
        Bukkit.broadcast(Component.text(event.getPlayer().getName() + "'s life faded away...", NamedTextColor.GRAY));
    }
}
