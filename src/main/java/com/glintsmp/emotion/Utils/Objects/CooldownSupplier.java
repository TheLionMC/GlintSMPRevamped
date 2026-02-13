package com.glintsmp.emotion.Utils.Objects;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface CooldownSupplier {
    Cooldown accept(Player player, Object[] context);
}