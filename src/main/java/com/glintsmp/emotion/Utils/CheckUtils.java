package com.glintsmp.emotion.Utils;

public class CheckUtils {
    public static int isInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
