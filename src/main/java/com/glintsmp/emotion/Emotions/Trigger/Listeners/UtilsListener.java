package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;

@EmotionListener
public class UtilsListener implements Listener {

    @EventHandler
    public void toolBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_TOOL_BREAK, player, event.getBrokenItem());
    }
}
