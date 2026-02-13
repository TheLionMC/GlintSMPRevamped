package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import com.glintsmp.emotion.Utils.ChatUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@EmotionListener
public class ChatListener implements Listener {

    @EventHandler
    public void playerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_CHATS, player, event.message());

        String message = ChatUtils.toString(event.message());
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(player) || !message.contains(target.getName())) continue;

            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_RECEIVES_MESSAGE, target,
                    event.message(), player);
        }
    }
}
