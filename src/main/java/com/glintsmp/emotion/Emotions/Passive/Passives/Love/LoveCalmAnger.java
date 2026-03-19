package com.glintsmp.emotion.Emotions.Passive.Passives.Love;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Passive.Passive;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Managers.EmotionManager;
import com.glintsmp.emotion.Relationship.RelationManager;
import com.glintsmp.emotion.Utils.TickUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class LoveCalmAnger implements Passive {

    private final Set<Player> players = new HashSet<>();
    private BukkitTask task;

    @Override
    public void apply(Player player, Emotion emotion) {
        players.add(player);
        if (task == null || task.isCancelled())
            task = start();
    }

    @Override
    public void clear(Player player) {
        players.remove(player);

        if (task != null && !task.isCancelled()
                && players.isEmpty())
            task.cancel();
    }

    private BukkitTask start() {
        return GlintSMP.runTaskTimer(tick -> {
            Random random = ThreadLocalRandom.current();

            for (Player player : players) {
                @NotNull Collection<Player> nearbyPlayers = player.getLocation().getNearbyPlayers(30);

                int change = random.nextInt(100);
                if (change >= 10) continue;

                for (Player nearby : nearbyPlayers) {
                    if (EmotionManager.getEmotionLevel(EmotionManager.ANGER, nearby.getUniqueId()) <= 70)
                        continue;

                    int remove = random.nextInt(3,9);
                    EmotionManager.decreaseEmotionLevel(EmotionManager.ANGER, player, remove);
                }
            }
            return true;
        },0, TickUtils.second(10));
    }
}
