package com.glintsmp.emotion.Commands.Commands.Testing;

import com.glintsmp.emotion.Commands.SubCommand;
import com.glintsmp.emotion.GlintSMP;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PlMessageCommand extends SubCommand {

    public PlMessageCommand() {
        super("plMessage", "sends a plugin message through the player",
                "glint.op");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) {
            player.sendMessage(Component.text("Invalid usage: /testing plMessage <message>"));
            return true;
        }

        send(player, args[1]);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return List.of();
    }

    public static void send(Player player, String message) {
        try {
            byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writeVarInt(baos, msgBytes.length);
            baos.write(msgBytes);
            player.sendPluginMessage(GlintSMP.getInstance(), GlintSMP.CHANNEL, baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeVarInt(ByteArrayOutputStream out, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.write(value);
    }

}
