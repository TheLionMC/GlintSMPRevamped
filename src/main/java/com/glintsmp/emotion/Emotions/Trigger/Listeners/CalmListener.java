package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Utils.TickUtils;
import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@EmotionListener
public class CalmListener implements Listener {

    @EventHandler
    public void playerEatsFood(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_EATS_FOOD, player,event.getItem());
    }

    @EventHandler
    public void playerEatsSleep(PlayerDeepSleepEvent event) {
        Player player = event.getPlayer();

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_SLEEPS, player);
    }

    @EventHandler
    public void playerEatsRegen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_REGEN_HEALTH, player,
                event.getAmount());
    }

    @EventHandler
    public void playerEatsMusic(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null || !(block.getState() instanceof Jukebox)) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        GlintSMP.runTaskLater(() -> {
            BlockState updatedState = block.getState();
            if (!(updatedState instanceof Jukebox jukebox)) return;

            if (!jukebox.hasRecord()) return;

            ItemStack record = jukebox.getRecord();
            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_LISTENS_TO_MUSIC_DISC, player, record);
        }, TickUtils.second(new Random().nextInt(20, 60)));
    }
}
