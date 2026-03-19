package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Utils.TickUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;
import java.util.Random;

@EmotionListener
public class EmotionTicks {

    public void init() {
        lonelyTicker();
        pingTicker();
        generalTicker();
        nearWarden();
    }

    private BukkitTask lonelyTicker() {
        return GlintSMP.runTaskTimer(tick -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Collection<Player> nearby = player.getLocation().getNearbyPlayers(160);

                if (nearby.size() == 1)
                    EmotionTriggerBus.fire(EmotionTrigger.ALONE_MINUTE, player);
                else EmotionTriggerBus.fire(EmotionTrigger.WITH_PLAYER_MINUTE, player);
            }

            return true;
        }, 0,TickUtils.minute(10));
    }

    private BukkitTask pingTicker() {
        return GlintSMP.runTaskTimer(tick -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                int ping = player.getPing();

                if (ping >= 250)
                    EmotionTriggerBus.fire(EmotionTrigger.PLAYER_EXPERIENCES_LAG_SPIKE, player, ping);
            }
            return true;
        }, 0, TickUtils.second(30));
    }

    private BukkitTask nearWarden() {
        return GlintSMP.runTaskTimer( tick -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Collection<LivingEntity> entityList = p.getLocation().getNearbyLivingEntities(30);
                for (LivingEntity e : entityList) {
                    if (e.getType() == EntityType.WARDEN) {
                        EmotionTriggerBus.fire(EmotionTrigger.NEAR_WARDEN, p);
                    }
                }
            }
            return true;
        }, 0, TickUtils.minute(5));
    }

    private BukkitTask generalTicker() {
        return  GlintSMP.runTaskTimer(tick -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                EmotionTriggerBus.fire(EmotionTrigger.GENERAL_DECREASE, p);
                EmotionTriggerBus.fire(EmotionTrigger.GENERAL_INCREASE, p);
            }
            return true;
        }, 0, TickUtils.hour(1));
    }
}
