package com.glintsmp.emotion.Utils;

import com.glintsmp.emotion.GlintSMP;
import net.kyori.adventure.text.Component;

public class ColorUtils {

    public static Component gradient(String content, String start, String end) {
        return GlintSMP.miniMessage.deserialize("<gradient:" + start + ":" + end + ">" + content + "</gradient>");
    }
}
