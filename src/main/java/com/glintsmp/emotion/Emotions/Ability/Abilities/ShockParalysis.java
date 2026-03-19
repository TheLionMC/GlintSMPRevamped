package com.glintsmp.emotion.Emotions.Ability.Abilities;

import com.glintsmp.emotion.Emotions.Ability.Ability;
import com.glintsmp.emotion.Emotions.Emotion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShockParalysis implements Ability {
    @Override
    public boolean activate(Player player, Emotion emotion) {
        boolean hitSomeone = false;

        for (Player target : player.getLocation().getNearbyPlayers(10)) {
            if (target.equals(player)) continue;
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 1);
            PotionEffect slow = new PotionEffect(PotionEffectType.SLOWNESS, 10*20, 1);
            PotionEffect nausea = new PotionEffect(PotionEffectType.NAUSEA, 10*20, 1);

            Location loc = target.getLocation().add(0, 1, 0);
            target.getWorld().spawnParticle(Particle.CRIT, loc, 30, 0.5, 0.5, 0.5, 0.05);
            target.getWorld().playSound(loc, Sound.ENTITY_GENERIC_HURT, 1.0f, 0.8f);

            target.getWorld().strikeLightningEffect(loc);
            target.setFreezeTicks(10 * 20);

            target.addPotionEffect(blindness);
            target.addPotionEffect(slow);
            target.addPotionEffect(nausea);
            target.damage(2.0, player);

            target.sendTitlePart(TitlePart.TITLE, Component.text("SHOCK", NamedTextColor.RED));

            hitSomeone = true;
        }

        return hitSomeone;
    }
}
