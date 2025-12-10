package com.glintsmp.emotion.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Passive.Passive;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Events.EmotionDecreaseEvent;
import com.glintsmp.emotion.Events.EmotionIncreaseEvent;
import com.glintsmp.emotion.Managers.EmotionManager;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Emotion {

    private final Map<EmotionTrigger, Pair<Type, Double>> triggerMap = new HashMap<>();

    private final Map<Integer, List<Passive>> passives = new HashMap<>();
    private final Map<UUID, Map<Integer, Map<Passive, Boolean>>> passiveState = new HashMap<>();

    private final String id;
    private final Ability ability;

    public Emotion(String id, Ability ability) {
        this.id = id;
        this.ability = ability;
    }

    public String getId() {
        return id;
    }

    public boolean activateAbility(Player player) {
        if (getCurrentValue(player) < 80 || ability == null) return false;

        if (ability.activate(player, this))
            setValue(player, 65);

        return true;
    }

    public Ability getAbility() {
        return ability;
    }

    public void addTrigger(EmotionTrigger trigger, Type type, double strength) {
        triggerMap.put(trigger, Pair.of(type, strength));
    }

    public void addPassive(int reqValue, Passive passive) {
        passives.computeIfAbsent(reqValue, integer -> new ArrayList<>())
                .add(passive);
    }

    public void updatePassives(Player player) {
        UUID uuid = player.getUniqueId();
        int currentVal = getCurrentValue(player);

        Map<Integer, Map<Passive, Boolean>> state = getPlayerPassiveState(uuid);

        for (Map.Entry<Integer, List<Passive>> entry : passives.entrySet()) {
            int req = entry.getKey();

            for (Passive passive : entry.getValue()) {

                boolean meetsRequirement = currentVal >= req;
                boolean wasActive = state
                        .computeIfAbsent(req, r -> new HashMap<>())
                        .getOrDefault(passive, false);

                if (meetsRequirement && !wasActive) {
                    passive.apply(player, this);
                    state.get(req).put(passive, true);
                }

                if (!meetsRequirement && wasActive) {
                    passive.clear(player);
                    state.get(req).put(passive, false);
                }
            }
        }
    }

    private Map<Integer, Map<Passive, Boolean>> getPlayerPassiveState(UUID uuid) {
        return passiveState.computeIfAbsent(uuid, u -> new HashMap<>());
    }

    public int getCurrentValue(Player player) {
        return EmotionManager.getEmotionLevel(this, player.getUniqueId());
    }

    public void setValue(Player player, int value) {
        EmotionManager.setEmotionLevel(this, player.getUniqueId(), value);
    }

    public void increase(Player player, int value) {
        EmotionIncreaseEvent event = new EmotionIncreaseEvent(player, this, value);
        Bukkit.getPluginManager().callEvent(event);

        updatePassives(player);
        onIncrease(player, value);
    }

    public void decrease(Player player, int value) {
        EmotionDecreaseEvent event = new EmotionDecreaseEvent(player, this, value);
        Bukkit.getPluginManager().callEvent(event);

        updatePassives(player);
        onDecrease(player, value);
    }

    public void onEvent(EmotionTrigger trigger, Player player, Object... context) {
        Pair<Type, Double> pair = triggerMap.get(trigger);
        if (pair == null) return;

        Type type = pair.getLeft();
        double strength = pair.getRight();

        if (type == Type.POSITIVE) increase(player, EmotionAlgorithm.getEmotionIncrease(strength));
        if (type == Type.NEGATIVE) decrease(player, EmotionAlgorithm.getEmotionDecrease(strength));
    }

    protected abstract void onIncrease(Player player, int value);
    protected abstract void onDecrease(Player player, int value);

    public enum Type {
        POSITIVE, NEGATIVE
    }
}