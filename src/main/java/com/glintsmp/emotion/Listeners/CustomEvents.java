package com.glintsmp.emotion.Listeners;

import com.glintsmp.emotion.Events.EmotionDecreaseEvent;
import com.glintsmp.emotion.Events.EmotionIncreaseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This is mainly here for testing purposes.
 * I created some custom events like {@link @EmotionDecreaseEvent}
 * so you can use this to test those out here :D
 * **/
public class CustomEvents implements Listener {

    @EventHandler
    public void onIncrease(EmotionIncreaseEvent event) {
        event.getPlayer().sendMessage("Test :D " + event.getIncreased() + " increased");
    }

    @EventHandler
    public void onDecrease(EmotionDecreaseEvent event) {
        event.getPlayer().sendMessage("Test :D " + event.getDecreased() + " decreased");
    }
}
