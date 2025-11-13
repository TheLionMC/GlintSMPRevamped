package com.glintsmp.emotion.Commands.Commands.Emotion;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Managers.EmotionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DecreaseEmotion extends SubCommand {

    public DecreaseEmotion() {
        super("decrease", "Decreases a player's emotion level",
                "glint.staff");
    }

    //decrease emotion player = <emotion> <player> <value>
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 4) {
            sender.sendMessage(Component.text("Usage: /emotion decrease <emotion> <player> <value>", NamedTextColor.RED));
            return true;
        }

        Emotion emotion = EmotionManager.getEmotion(args[1]);
        if (emotion == null) {
            sender.sendMessage(Component.text("Emotion " + args[1] + " does not exist!", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(Component.text("Player " + args[2] + " is not online!", NamedTextColor.RED));
            return true;
        }

        int val;
        try {
            val = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("You gotta type in a valid number you dumb fuck!", NamedTextColor.RED));
            return true;
        }

        if (val < 1 || val > 101) {
            sender.sendMessage(Component.text("Value must be between 1 and 100!", NamedTextColor.RED));
            return true;
        }

        EmotionManager.decreaseEmotionLevel(emotion, target, val);
        sender.sendMessage(Component.text("Decreased Emotion level of " + target.getName() + " by " + val, NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> collection = new ArrayList<>();
        if (args.length == 2) {
            for (Emotion emotion : EmotionManager.getEmotions())
                collection.add(emotion.getId());
        }

        if (args.length == 3) {
            for (Player player : Bukkit.getOnlinePlayers())
                collection.add(player.getName());
        }

        return collection;
    }
}
