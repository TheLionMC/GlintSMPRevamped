package com.glintsmp.emotion.Commands.Commands.Trust;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Emotions.Emotion;
import com.glintsmp.emotion.Emotions.EmotionManager;
import com.glintsmp.emotion.Trust.TrustManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TrustAdd extends SubCommand {

    public TrustAdd() {
        super("add", "Add someone to your trustlist.");
    }

    // Add a player to your trustlist
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            sender.sendMessage(Component.text("Usage: /trust add <player>", NamedTextColor.RED));
            return true;
        }
        Player targetplayer = Bukkit.getPlayer(sender.getName());
        UUID targetplayeruuid = Objects.requireNonNull(targetplayer).getUniqueId();

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null) {
            sender.sendMessage(Component.text("Player " + args[2] + " is not online!", NamedTextColor.RED));
            return true;
        }
        UUID adduuid = player.getUniqueId();

        TrustManager.addTrusted(targetplayeruuid, adduuid);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
