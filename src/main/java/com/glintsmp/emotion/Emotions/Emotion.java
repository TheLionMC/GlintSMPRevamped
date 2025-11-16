package com.glintsmp.emotion.Emotions;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Passive.Passive;
import com.glintsmp.emotion.Managers.EmotionManager;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Emotion {

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
        updatePassives(player);
        onIncrease(player, value);
    }

    public void decrease(Player player, int value) {
        updatePassives(player);
        onDecrease(player, value);
    }

    protected abstract void onIncrease(Player player, int value);
    protected abstract void onDecrease(Player player, int value);
}