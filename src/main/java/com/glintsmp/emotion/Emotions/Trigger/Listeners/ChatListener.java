package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@EmotionListener
public class ChatListener implements Listener {

    @EventHandler
    public void playerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_CHATS, player, event.message());
    }
}
