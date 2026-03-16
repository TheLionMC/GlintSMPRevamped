package com.glintsmp.emotion.RelationshipAlgorithm.Triggers;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Ensures TriggerRegistry is populated and performs a harmless access of all triggers
 * when a player joins (so the classes are referenced at runtime).
 */
public class TriggerEventHandler implements Listener {

    public TriggerEventHandler() {
        // Ensure registry populated at construction time
        TriggerRegistry.registerAll();
        GlintSMP.logger.info("TriggerRegistry: registered " + TriggerRegistry.all().size() + " triggers.");

        // Register the detailed trigger listener which fires triggers on game events
        org.bukkit.Bukkit.getPluginManager().registerEvents(new TriggerListener(), GlintSMP.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Touch the registry so the classes are referenced; do not modify game state.
        int count = 0;
        for (Trigger t : TriggerRegistry.all()) {
            if (t != null) {
                // call getId() to reference the object
                String id = t.getId();
                if (id != null) count++;
            }
        }
        GlintSMP.logger.info("TriggerRegistry: player " + e.getPlayer().getName() + " saw " + count + " triggers.");
    }
}
