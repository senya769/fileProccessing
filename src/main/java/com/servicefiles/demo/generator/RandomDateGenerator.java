package com.servicefiles.demo.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RandomDateGenerator implements ValueGenerator<String> {
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final int daysBack;

    public RandomDateGenerator(int daysBack) {
        this.daysBack = daysBack;
    }

    @Override
    public String generate() {
        int days = RANDOM.nextInt(daysBack + 1);
        LocalDate date = LocalDate.now().minusDays(days);
        return date.format(FORMATTER);
    }
}
