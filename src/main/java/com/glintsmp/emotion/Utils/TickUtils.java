package com.glintsmp.emotion.Utils;

public class TickUtils {

    public static long second(long amount) {
        return convert(amount, Time.SECOND);
    }

    public static long minute(long amount) {
        return convert(amount, Time.MINUTE);
    }

    public static long hour(long amount) {
        return convert(amount, Time.HOUR);
    }

    public static long convert(long amount, Time time) {
        return Math.multiplyExact(amount, time.ticks);
    }

    public enum Time {
        SECOND(20),
        MINUTE(12_00),
        HOUR(72_000);

        public final long ticks;

        Time(long ticks) {
            this.ticks = ticks;
        }

        public long getValue() {
            return ticks;
        }
    }
}
