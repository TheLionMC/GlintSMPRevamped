package com.glintsmp.emotion.Afk;

import org.bukkit.Location;

import java.util.*;

public class AfkStatus {

    private static final double MIN_DISPLACEMENT = 2.0;
    private static final long INTERACTION_GRACE_MS = 60_000;

    private final MovementHistory history = new MovementHistory();
    private long lastTimeInteracted = 0;

    public void setLastTimeInteracted(long lastTimeInteracted) {
        this.lastTimeInteracted = lastTimeInteracted;
    }

    public boolean isAfk() {
        long now = System.currentTimeMillis();
        boolean noInteraction = (now - lastTimeInteracted) > INTERACTION_GRACE_MS;
        boolean hardlyMoved   = history.getTotalPathLength() < MIN_DISPLACEMENT;
        boolean mostlyGrounded = history.getGroundRatio() > 0.80;
        return noInteraction && hardlyMoved && mostlyGrounded;
    }

    public MovementHistory getHistory() {
        return history;
    }

    public static class MovementHistory {
        private static final long WINDOW_MS = 10_000;
        private static final int MAX_SAMPLES = 60;

        private final Deque<MovementSample> samples = new ArrayDeque<>();

        public void record(MovementSample sample) {
            samples.addLast(sample);
            if (samples.size() > MAX_SAMPLES) samples.pollFirst();
            long cutoff = System.currentTimeMillis() - WINDOW_MS;
            while (!samples.isEmpty() && samples.peekFirst().timestamp() < cutoff)
                samples.pollFirst();
        }

        public double getTotalDisplacement() {
            if (samples.size() < 2) return 0;
            MovementSample first = samples.peekFirst();
            MovementSample last = samples.peekLast();
            return first.location().distance(last.location());
        }

        public double getTotalPathLength() {
            if (samples.size() < 2) return 0;
            double total = 0;
            MovementSample prev = null;
            for (MovementSample s : samples) {
                if (prev != null)
                    total += prev.location().distance(s.location());
                prev = s;
            }
            return total;
        }

        public double getGroundRatio() {
            if (samples.isEmpty()) return 1.0;
            long grounded = samples.stream().filter(MovementSample::isOnGround).count();
            return (double) grounded / samples.size();
        }

        public Deque<MovementSample> getSamples() {
            return samples;
        }
    }

    public record MovementSample(Location location, long timestamp,
                                 float yaw, float pitch, boolean isOnGround) {}
}
