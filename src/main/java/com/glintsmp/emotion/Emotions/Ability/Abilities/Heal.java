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
import org.bukkit.entity.Player;

public class Heal implements Ability {

    @Override
    public boolean activate(Player player, Emotion emotion) {
        Location center = player.getLocation();

        GlintSMP.runTaskTimer(tick -> {
            if (tick >= 5)
                return false;

            for (int i = 1; i <= 6; i++) {
                int finalI = i;
                GlintSMP.runTaskLater(() ->
                                ParticleUtils.spawnCircle(center,
                                        new Particle.DustOptions(Color.fromRGB(227, 93, 202), 1.5F), finalI * 1.5, finalI * 150),
                        (i - 1) * 2);
            }

            center.getWorld().playSound(center, Sound.BLOCK_CONDUIT_AMBIENT_SHORT,5,1);

            for (Player target : center.getNearbyPlayers(7,3,7)) {
                if (!TrustManager.isTrusted(player.getUniqueId(), target.getUniqueId())) continue;

                target.heal(3);
            }

            return true;
        },0,30);
        return true;
    }
}
