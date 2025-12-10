package com.glintsmp.emotion.Emotions.Trigger;

import com.glintsmp.emotion.Emotions.Emotion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmotionTriggerBus {

    private static final Map<EmotionTrigger, List<Emotion>> listeners = new HashMap<>();

    public static void subscribe(EmotionTrigger trigger, Emotion emotion) {
        listeners.computeIfAbsent(trigger, t -> new ArrayList<>()).add(emotion);
    }

    public static void fire(EmotionTrigger trigger, Player player, Object... context) {
        List<Emotion> list = listeners.get(trigger);
        if (list == null) return;

        for (Emotion emotion : list)
            emotion.onEvent(trigger, player, context);
    }
}
