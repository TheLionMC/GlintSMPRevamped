package com.glintsmp.emotion.Managers;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.Emotions.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class ActionbarManager extends BukkitRunnable {

    public static BukkitTask task;

    public static void initialize(Plugin plugin) {
        task = new ActionbarManager().runTaskTimer(plugin, 0, 1);
    }

    private final Map<Class<?>, Integer> unicodeMap = Map.of(
            Anger.class, 0, Boredom.class, 10,
            Confidence.class, 20, Exception.class, 30,
            Fear.class, 40, Loneliness.class, 50,
            Love.class, 60, Sadness.class, 70,
            Hatred.class, 80, Surprise.class, 90);

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers())
            sendActionbar(player);
    }

    private void sendActionbar(Player player) {
        Emotion highest = EmotionManager.getHighest(player);
        int start = unicodeMap.getOrDefault(highest.getClass(), 900);

        int value = Math.min(highest.getCurrentValue(player) / 10, 9);
        int codepoint = 0xE000 + start + value;
        String unicode = new String(Character.toChars(codepoint));

        player.sendActionBar(Component.text(unicode));
    }
}