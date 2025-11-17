package com.servicefiles.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MergerService {

    private static final Logger logger = LoggerFactory.getLogger(MergerService.class);

    public void mergeFiles(File outputDir, File mergedFile, String removeString) {
        logger.info("Параметр для удаления {}", removeString);
        File[] files = outputDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) return;

        int removed = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mergedFile))) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (removeString != null && line.contains(removeString)) {
                            removed++;
                            continue;
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при объединении файлов", e);
        }

        logger.info("Объединение завершено. Удалено строк: {}", removed);
    }
}
