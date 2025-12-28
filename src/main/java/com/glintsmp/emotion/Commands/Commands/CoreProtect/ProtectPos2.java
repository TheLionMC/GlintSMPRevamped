package com.glintsmp.emotion.Commands.Commands.CoreProtect;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.Utils.Pair;
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

public class ProtectPos2 extends SubCommand {

    public ProtectPos2() {
        super("pos2", "sets the second location for the protected region", "glint.staff");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        Pair<Location, Location> region = ProtectCommand.region.get(player.getUniqueId());
        if (region == null) region = Pair.empty();

        ProtectCommand.region.put(player.getUniqueId(), region);

        region.setRight(player.getLocation());
        player.sendMessage(Component.text("Set position 2 to your location", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return List.of();
    }
}
