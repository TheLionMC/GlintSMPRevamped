package com.glintsmp.emotion.Emotions.Trigger.Listeners;

import com.glintsmp.emotion.Emotions.Trigger.EmotionListener;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTrigger;
import com.glintsmp.emotion.Emotions.Trigger.EmotionTriggerBus;
import com.glintsmp.emotion.Managers.TrustManager;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

@EmotionListener
public class DeathListener implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = player.getKiller();
        if (killer == null || TrustManager.isTrusted(player.getUniqueId(), killer.getUniqueId())) return;

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_DEATH_BY_PLAYER, player, killer);
    }

    @EventHandler
    public void playerKillEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = player.getKiller();
        if (killer == null || TrustManager.isTrusted(player.getUniqueId(), killer.getUniqueId())) return;

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_KILLED_PLAYER, killer, player);
    }

    @EventHandler
    public void playerKillEvent(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Tameable tameable)) return;

        AnimalTamer owner = tameable.getOwner();
        if (!(owner instanceof Player player)) return;

        Player killer = tameable.getKiller();
        if (killer == null || TrustManager.isTrusted(owner.getUniqueId(), killer.getUniqueId())) return;

        EmotionTriggerBus.fire(EmotionTrigger.PET_KILLED, player, tameable.getKiller());
    }

    @EventHandler
    public void deathByExplosion(PlayerDeathEvent event) {
        Player p = event.getEntity();

        EntityDamageEvent e = p.getLastDamageCause();

        assert e != null;
        EntityDamageEvent.DamageCause damageCause = e.getCause();

        if (!(damageCause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || damageCause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))) return;

        EmotionTriggerBus.fire(EmotionTrigger.PLAYER_DEATH_EXPLOSION, p);
    }


    @EventHandler
    public void nearbyDeath(PlayerDeathEvent event) {
        Player dead = event.getPlayer();

        for (Player witness : dead.getLocation().getNearbyPlayers(32)) {
            if (witness.equals(dead)) continue;
            EmotionTriggerBus.fire(EmotionTrigger.PLAYER_WITNESS_DEATH, witness, dead);
        }
    }
}
