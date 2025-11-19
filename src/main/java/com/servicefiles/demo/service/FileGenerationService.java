package com.servicefiles.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.servicefiles.demo.config.GeneratorConfig;
import com.servicefiles.demo.generator.RandomCyrillicGenerator;
import com.servicefiles.demo.generator.RandomDateGenerator;
import com.servicefiles.demo.generator.RandomEvenIntGenerator;
import com.servicefiles.demo.generator.RandomFloatGenerator;
import com.servicefiles.demo.generator.RandomLatinGenerator;
import com.servicefiles.demo.generator.ValueGenerator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class FileGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(FileGenerationService.class);

    private final GeneratorConfig config;
    private final List<ValueGenerator<?>> generators;

    public FileGenerationService(GeneratorConfig config) {
        this.config = config;

        // Инициализация генераторов с параметрами из config
        this.generators = Arrays.asList(
                new RandomDateGenerator(config.getDaysBack()),
                new RandomLatinGenerator(config.getLatinLength()),
                new RandomCyrillicGenerator(config.getCyrillicLength()),
                new RandomEvenIntGenerator(config.getMinInt(), config.getMaxInt()),
                new RandomFloatGenerator(config.getMinFloat(), config.getMaxFloat())
        );
    }

    public void generateFiles() {
        File outputDir = new File(config.getOutputDir());

        if (!outputDir.exists() && !outputDir.mkdirs()) {
            logger.warn("Не удалось создать директорию: {}", outputDir.getAbsolutePath());
        }

        logger.info("Запуск генерации {} файлов...", config.getFileCount());

        IntStream.rangeClosed(1, config.getFileCount())
                .parallel()  // ← многопоточность
                .forEach(i -> {
                    File file = new File(outputDir, "file_" + i + ".txt");
                    generateFile(file);
                    logger.info("Создан файл: {}", file.getName());
                });

        logger.info("Генерация всех файлов завершена.");
    }


    private void generateFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < config.getLinesPerFile(); i++) {
                writer.write(generateLine());
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Ошибка записи файла: {}", file.getAbsolutePath(), e);
            throw new RuntimeException("Ошибка записи файла: " + file, e);
        }
    }

    private String generateLine() {
        StringBuilder sb = new StringBuilder();

        for (ValueGenerator<?> generator : generators) {
            Object value = generator.generate();

            if (value instanceof Double d) {
                // Форматируем дробные числа с 8 знаками после запятой и заменой точки на запятую
                sb.append(String.format("%.8f", d).replace('.', ','));
            } else {
                sb.append(value.toString());
            }

            sb.append(config.getSeparator());
        }

        return sb.toString();
    }
}