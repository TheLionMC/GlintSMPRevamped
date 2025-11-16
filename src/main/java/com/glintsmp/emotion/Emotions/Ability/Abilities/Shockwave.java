package com.glintsmp.emotion.Emotions.Ability.Abilities;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Emotion;

import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Managers.TrustManager;
import com.glintsmp.emotion.Utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Shockwave implements Ability {

    @Override
    public boolean activate(Player player, Emotion emotion) {
        int value = emotion.getCurrentValue(player);
        Location center = player.getLocation().clone();

        player.playSound(center, Sound.ITEM_MACE_SMASH_GROUND_HEAVY,5,1);

        int damage = value >= 100 ? 35 : 25;
        int knockback = value >= 100 ? 4 : 2;

        for (int i = 1; i <= 11; i++) {
            int finalI = i;
            GlintSMP.runTaskLater(() ->
                    ParticleUtils.spawnCircle(center,
                            new Particle.DustOptions(Color.fromRGB(25 * (finalI - 1), 0, 0), 1.5F), finalI * 1.5, finalI * 150),
                    (i - 1) * 2);
        }

        for (LivingEntity target : center.getNearbyLivingEntities(12,3,12)) {
            if (target.equals(player) || TrustManager.isTrusted(player.getUniqueId(), target.getUniqueId())) continue;

            Vector direction = target.getLocation().toVector()
                    .subtract(player.getLocation().toVector())
                    .normalize().multiply(knockback);
            direction.setY(1);

            target.setVelocity(direction);
            target.damage(damage, player);
        }

        return true;
    }
}
