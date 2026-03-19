package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {

    @EventHandler
    public void playerAdvancement(PlayerAdvancementDoneEvent event) {
        AdvancementDisplay display = event.getAdvancement().getDisplay();
        if (display == null) return;

        AdvancementDisplay.Frame advancement = display.frame();

        if (advancement == AdvancementDisplay.Frame.CHALLENGE)
            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_TOAST, event.getPlayer());
        else
            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_RECEIVE_ADVANCEMENT_NORMAL, event.getPlayer());
    }

    @EventHandler
    public void playerEnterEnd(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        Player player = event.getPlayer();

        if (advancement.displayName().equals(Component.text("The End?"))) {
            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_ENTER_END, player);
        } else if (advancement.displayName().equals(Component.text("We Need to Go Deeper"))) {
            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_ENTER_NETHER, player);
        }
    }
}
