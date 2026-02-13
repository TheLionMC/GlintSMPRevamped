package com.glintsmp.emotion.Utils;

import com.glintsmp.emotion.GlintSMP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ChatUtils {

    public static final LegacyComponentSerializer componentSerializer = LegacyComponentSerializer.builder().build();

    public static Component gradient(String content, String start, String end) {
        return GlintSMP.miniMessage.deserialize("<gradient:" + start + ":" + end + ">" + content + "</gradient>");
    }

    public static String toString(Component text) {
        return componentSerializer.serialize(text);
    }
}
