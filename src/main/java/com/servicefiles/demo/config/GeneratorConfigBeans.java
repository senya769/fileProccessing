package com.servicefiles.demo.config;

import com.servicefiles.demo.generator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfigBeans {

    private final GeneratorConfig config;

    public GeneratorConfigBeans(GeneratorConfig config) {
        this.config = config;
    }

    @Bean
    public RandomEvenIntGenerator randomEvenIntGenerator() {
        return new RandomEvenIntGenerator(config.getMinInt(), config.getMaxInt());
    }

    @Bean
    public RandomDateGenerator randomDateGenerator() {
        return new RandomDateGenerator(config.getDaysBack());
    }

    @Bean
    public RandomLatinGenerator randomLatinGenerator() {
        return new RandomLatinGenerator(config.getLatinLength());
    }

    @Bean
    public RandomCyrillicGenerator randomCyrillicGenerator() {
        return new RandomCyrillicGenerator(config.getCyrillicLength());
    }

    @Bean
    public RandomFloatGenerator randomFloatGenerator() {
        return new RandomFloatGenerator(config.getMinFloat(), config.getMaxFloat());
    }
}
