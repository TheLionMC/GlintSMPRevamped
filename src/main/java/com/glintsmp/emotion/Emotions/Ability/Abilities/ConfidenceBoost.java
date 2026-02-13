package com.glintsmp.emotion.Emotions.Ability.Abilities;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Managers.TrustManager;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ConfidenceBoost implements Ability {

    @Override
    public boolean activate(Player player, Emotion emotion) {
        Collection<Player> players = player.getLocation().getNearbyPlayers(7);

        for (Player target : players) {
            if (!TrustManager.isTrusted(player.getUniqueId(), target.getUniqueId())) continue;

            Random random = ThreadLocalRandom.current();
            target.spawnParticle(Particle.TRIAL_OMEN, target.getEyeLocation(),100, random.nextDouble(0,1.5),
                    random.nextDouble(0,1.5), random.nextDouble(0,1.5));

            target.playSound(target.getLocation(), Sound.BLOCK_BREWING_STAND_BREW,1,1);

            target.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE,20 * 60 * 3,0));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20 * 60 * 3,0));
        }

        return true;
    }
}
