package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import com.glintsmp.emotion.Managers.TrustManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@EmotionListener
public class DamageListener implements Listener {

    @EventHandler
    public void playerDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) ||
                !(event.getEntity() instanceof Player player)) return;

        if (TrustManager.isTrusted(damager.getUniqueId(), player.getUniqueId())) return;

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_DAMAGED_BY_PLAYER, player,
                damager, event.getDamage());
    }
}
