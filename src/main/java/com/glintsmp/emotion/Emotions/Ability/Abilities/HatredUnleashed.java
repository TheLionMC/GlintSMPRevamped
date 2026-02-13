package com.glintsmp.emotion.Emotions.Ability.Abilities;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Managers.TrustManager;
import com.glintsmp.emotion.Utils.ParticleUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class HatredUnleashed implements Ability {

    @Override @SuppressWarnings("all")
    public boolean activate(Player player, Emotion emotion) {
        GlintSMP.runTaskTimer(tick -> {
            if (tick >= 20)
                return true;

            Location center = player.getLocation();
            center.add(0,1,0);
            int strenght = -1;

            Collection<LivingEntity> entities = center.getNearbyLivingEntities(2.5);
            for (LivingEntity target : entities) {
                if (TrustManager.isTrusted(player.getUniqueId(), target.getUniqueId())) continue;

                if (target instanceof Player)
                    strenght++;

                target.damage(2, DamageSource.builder(DamageType.ON_FIRE)
                        .withDirectEntity(player).build());
            }

            ParticleUtils.spawnCircle(center, Particle.FLAME,3,100,0,0,0,0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5, Math.min(2, strenght)));
            return true;
        },0,5);
        return true;
    }
}
