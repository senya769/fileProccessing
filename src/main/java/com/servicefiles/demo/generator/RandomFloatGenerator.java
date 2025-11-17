package com.servicefiles.demo.generator;

import java.util.Random;

public class RandomFloatGenerator implements ValueGenerator<Double> {
    private static final Random RANDOM = new Random();
    private final double min;
    private final double max;

    public RandomFloatGenerator(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Double generate() {
        return min + RANDOM.nextDouble() * (max - min);
    }
}
