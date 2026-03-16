package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

@EmotionListener
public class EnvListener implements Listener {

    @EventHandler
    public void playerDefeatMinecraftBoss(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof EnderDragon)
                && !(event.getEntity() instanceof ElderGuardian
                && !((event.getEntity()) instanceof Warden)
                && !(event.getEntity() instanceof Wither))) return;
        Player player = event.getEntity().getKiller();

        Entity e = event.getEntity();

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_DEFEATS_BOSS, player, e);
    }
}
