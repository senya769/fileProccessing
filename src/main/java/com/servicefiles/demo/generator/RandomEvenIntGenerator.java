package com.servicefiles.demo.generator;

import java.util.Random;

public class RandomEvenIntGenerator implements ValueGenerator<Integer> {
    private static final Random RANDOM = new Random();
    private final int min;
    private final int max;

    public RandomEvenIntGenerator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer generate() {
        int value = min + RANDOM.nextInt(max - min + 1);
        return (value % 2 == 0) ? value : value + 1;
    }
}
