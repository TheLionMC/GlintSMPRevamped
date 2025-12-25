package com.glintsmp.emotion.Commands.Commands.Lives;

import com.glintsmp.emotion.Commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetLivesCommand extends SubCommand {

    public GetLivesCommand() {
        super("get", "Gets the lives of a specific Player (for staff), gets your own lives (for normal Player)");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
