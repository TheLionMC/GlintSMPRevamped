package com.glintsmp.emotion.Utils;

public class Cooldown {

    private final long startTime;
    private final long durationMillis;

    public Cooldown(long timeSeconds) {
        this.startTime = System.currentTimeMillis();
        this.durationMillis = timeSeconds * 1000;
    }

    public long getTimeLeft() {
        long elapsed = System.currentTimeMillis() - startTime;
        long timeLeft = durationMillis - elapsed;
        return Math.max(0, timeLeft);
    }

    public boolean hasFinished() {
        return getTimeLeft() >= 0;
    }
}

