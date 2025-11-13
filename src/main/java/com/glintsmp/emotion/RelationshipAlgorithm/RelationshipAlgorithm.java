package com.glintsmp.emotion.RelationshipAlgorithm;

public class RelationshipAlgorithm {

    public static int calculateIncrease(int current, int change) {
        double factor = 1.0 - (current / 150.0);
        return (int) Math.round(change * factor);
    }

    public static int calculateDecrease(int current, int change) {
        double factor = 1.0 + (current / 200.0);
        return (int) Math.round(change * factor);
    }
}
