package com.glintsmp.emotion.Emotions.Ability.Abilities;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Managers.TrustManager;
import com.glintsmp.emotion.Utils.TickUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AdrenalineRush implements Ability {

    @Override
    public boolean activate(Player player, Emotion emotion) {
        boolean facingEnemy = false;

        for (Player other : player.getLocation().getNearbyPlayers(20.0)) { // how many blocks away they may be detected
            if (TrustManager.isTrusted(player.getUniqueId(), other.getUniqueId())) continue;

            double angle = getAngleTo(player, other);
            if (angle <= 45.0) { // angle that players are detected in
                facingEnemy = true;
                break;
            }
        }

        if (facingEnemy) player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, (int) TickUtils.second(2), 4));
        else player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) TickUtils.second(2), 10));


        return true;
    }

    private double getAngleTo(Player player, Player target) {
        var dir = player.getLocation().getDirection().normalize();
        var toTarget = target.getLocation().toVector()
                .subtract(player.getLocation().toVector()).normalize();

        return Math.toDegrees(Math.acos(Math.clamp(dir.dot(toTarget), -1.0, 1.0)));
    }
}
