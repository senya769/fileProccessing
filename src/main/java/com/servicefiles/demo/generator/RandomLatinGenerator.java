package com.servicefiles.demo.generator;

import java.util.Random;

public class RandomLatinGenerator implements ValueGenerator<String> {
    private static final Random RANDOM = new Random();
    private final int length;

    public RandomLatinGenerator(int length) {
        this.length = length;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            boolean upper = RANDOM.nextBoolean();
            char letter = (char) ((upper ? 'A' : 'a') + RANDOM.nextInt(26));
            sb.append(letter);
        }
        return sb.toString();
    }
}
