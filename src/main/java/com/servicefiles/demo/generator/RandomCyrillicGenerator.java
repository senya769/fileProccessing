package com.servicefiles.demo.generator;

import java.util.Random;

public class RandomCyrillicGenerator implements ValueGenerator<String> {
    private static final Random RANDOM = new Random();
    private final int length;

    public RandomCyrillicGenerator(int length) {
        this.length = length;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            boolean upper = RANDOM.nextBoolean();
            char base = upper ? 'А' : 'а';
            char letter = (char) (base + RANDOM.nextInt(32));
            sb.append(letter);
        }
        return sb.toString();
    }
}
