package com.glintsmp.emotion.Emotions.Passive.Passives.Love;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Passive.Passive;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Relationship.RelationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LoveRegenPassive implements Passive {

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
            for (Player player : players) {
                @NotNull Collection<Player> nearbyPlayers = player.getLocation().getNearbyPlayers(40);

                for (Player nearby : nearbyPlayers) {
                    if (!RelationManager.hasHighRelation(player, nearby)) continue;

                    player.heal(0.2);
                    player.sendMessage(Component.text("You healed because aura! (and LoveRegenPassive.java :D)",
                            NamedTextColor.LIGHT_PURPLE));
                }
            }
            return true;
        },0,20);
    }
}
