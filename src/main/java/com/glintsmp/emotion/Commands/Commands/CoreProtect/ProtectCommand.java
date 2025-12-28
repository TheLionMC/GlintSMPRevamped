package com.glintsmp.emotion.Commands.Commands.CoreProtect;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.CoreProtect.CoreProtectManager;
import com.glintsmp.emotion.CoreProtect.ProtectedRegion;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProtectCommand extends SubCommand {

    public static final Map<UUID, Pair<Location, Location>> region = new HashMap<>();

    public ProtectCommand() {
        super("protect", "protects the selected region", "");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            player.sendMessage(Component.text("Wrong usage: /core protect name"));
            return true;
        }

        Pair<Location, Location> region = ProtectCommand.region.get(player.getUniqueId());
        if (region == null) {
            player.sendMessage(Component.text("You do no have any positions selected, make sure to select 2 positions using /core pos1 | pos2",
                    NamedTextColor.RED));
            return true;
        }

        Location corner1 = region.getLeft();
        Location corner2 = region.getRight();

        if (corner1 == null || corner2 == null) {
            player.sendMessage(Component.text("Make sure to have 2 positions selected",
                    NamedTextColor.RED));
            return true;
        }

        ProtectedRegion selected = CoreProtectManager.createRegion(corner1, corner2);
        CoreProtectManager.protect(args[1], selected);

        player.sendMessage(Component.text("Successfully protected region: " + args[1]));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return List.of();
    }
}
