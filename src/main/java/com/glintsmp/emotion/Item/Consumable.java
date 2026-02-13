package com.glintsmp.emotion.Item;

import org.bukkit.event.player.PlayerItemConsumeEvent;

public interface Consumable {
    void onConsume(PlayerItemConsumeEvent event);
}
