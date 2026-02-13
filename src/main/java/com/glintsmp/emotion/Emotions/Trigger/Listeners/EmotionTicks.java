package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Utils.TickUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

@EmotionListener
public class EmotionTicks {

    public void init() {
        lonelyTicker();
        pingTicker();
    }

    private BukkitTask lonelyTicker() {
        return GlintSMP.runTaskTimer(tick -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Collection<Player> nearby = player.getLocation().getNearbyPlayers(160);

                if (nearby.size() != 1)
                    EmotionTriggerBus.fire(EmotionTrigger.ALONE_MINUTE, player);
                else EmotionTriggerBus.fire(EmotionTrigger.WITH_PLAYER_MINUTE, player);
            }

            return true;
        }, 0,TickUtils.minute(1));
    }

    private BukkitTask pingTicker() {
        return GlintSMP.runTaskTimer(tick -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                int ping = player.getPing();

                if (ping >= 300)
                    EmotionTriggerBus.fire(EmotionTrigger.PLAYER_EXPERIENCES_LAG_SPIKE, player, ping);
            }
            return true;
        }, 0, TickUtils.second(30));
    }
}
