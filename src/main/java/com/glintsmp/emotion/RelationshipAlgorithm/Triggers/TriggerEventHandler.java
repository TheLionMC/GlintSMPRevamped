package com.glintsmp.emotion.RelationshipAlgorithm.Triggers;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Ensures TriggerRegistry is populated and performs harmless access of all triggers
 * when a player joins (so the classes are referenced at runtime).
 */
public class TriggerEventHandler implements Listener {

    public TriggerEventHandler() {
        TriggerRegistry.registerAll();
        Bukkit.getLogger().info("TriggerRegistry: registered " + TriggerRegistry.all().size() + " triggers.");

        Bukkit.getPluginManager().registerEvents(new TriggerListener(), GlintSMP.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        int count = 0;
        for (Trigger t : TriggerRegistry.all()) {
            if (t != null) {
                String id = t.getId();
                if (id != null) count++;
            }
        }
        Bukkit.getLogger().info("TriggerRegistry: player " + e.getPlayer().getName() + " saw " + count + " triggers.");
    }
}
