package com.glintsmp.emotion.Emotions.Trigger;

import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Utils.Objects.Cooldown;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class EmotionTriggerBus {

    private static final Map<EmotionTrigger, List<Emotion>> listeners = new HashMap<>();
    private static final Map<UUID, Map<EmotionTrigger, Cooldown>> cooldowns = new HashMap<>();

    public static void subscribe(EmotionTrigger trigger, Emotion emotion) {
        listeners.computeIfAbsent(trigger, t -> new ArrayList<>()).add(emotion);
    }

    public static void fire(EmotionTrigger trigger, Player player, Object... context) {
        List<Emotion> list = listeners.get(trigger);
        if (list == null) return;

        for (Emotion emotion : list)
            emotion.onEvent(trigger, player, context);
    }

    @Nullable
    private Cooldown getCooldown(UUID uuid, EmotionTrigger trigger) {
        return getCooldowns(uuid).get(trigger);
    }

    @NotNull
    private Map<EmotionTrigger, Cooldown> getCooldowns(UUID uuid) {
        return cooldowns.getOrDefault(uuid, new HashMap<>());
    }

    private void setCooldown(UUID uuid, EmotionTrigger emotionTrigger, Cooldown cooldown) {
        cooldowns.computeIfAbsent(uuid, uuid1 -> new HashMap<>())
                .put(emotionTrigger, cooldown);
    }
}
