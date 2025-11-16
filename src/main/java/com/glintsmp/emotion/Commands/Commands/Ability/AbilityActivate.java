package com.glintsmp.emotion.Commands.Commands.Ability;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Managers.EmotionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbilityActivate extends SubCommand {

    public AbilityActivate() {
        super("activate", "Will activate the highest emotion's ability");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        Emotion highest = EmotionManager.getHighest(player);

        if (!highest.activateAbility(player))
            player.sendMessage(Component.text("Your feelings arent strong enough.").color(NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
