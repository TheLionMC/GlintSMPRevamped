package com.glintsmp.emotion.Commands.Commands.CoreProtect;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Utils.Objects.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ProtectPos1 extends SubCommand {

    public ProtectPos1() {
        super("pos1", "sets the first location for the protected region", "glint.staff");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        Pair<Location, Location> region = ProtectCommand.region.get(player.getUniqueId());
        if (region == null) region = Pair.empty();

        ProtectCommand.region.put(player.getUniqueId(), region);

        region.setLeft(player.getLocation());
        player.sendMessage(Component.text("Set position 1 to your location", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return List.of();
    }
}
