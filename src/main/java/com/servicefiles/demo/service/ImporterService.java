package com.servicefiles.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Service
public class ImporterService {

    private static final Logger logger = LoggerFactory.getLogger(ImporterService.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS records";

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE records (" +
            "id SERIAL PRIMARY KEY, " +
            "date_value VARCHAR(20), " +
            "latin_text VARCHAR(20), " +
            "cyr_text VARCHAR(20), " +
            "int_value INTEGER, " +
            "float_value DOUBLE PRECISION)";

    private static final String INSERT_SQL =
            "INSERT INTO records(date_value, latin_text, cyr_text, int_value, float_value) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final int LOG_STEP = 10_000;

    /**
     * Основной метод импорта.
     */
    public void importFile(File file) {
        if (!file.exists()) {
            logger.error("Файл для импорта не найден: {}", file.getAbsolutePath());
            return;
        }

        int totalLines = countLines(file);
        logger.info("Старт импорта. Всего строк в файле: {}", totalLines);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            resetTable(conn);
            importData(conn, file, totalLines);
        } catch (Exception e) {
            logger.error("Ошибка при импорте файла", e);
        }
    }

    /**
     * Импорт данных построчно.
     */
    private void importData(Connection conn, File file, int totalLines) {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL);
             BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            int imported = 0;

            while ((line = reader.readLine()) != null) {
                if (!processLine(stmt, line)) {
                    continue;
                }

                imported++;

                if (imported % LOG_STEP == 0) {
                    logger.info("Импортировано {} / {} строк (осталось: {})",
                            imported, totalLines, totalLines - imported);
                }
            }

            logger.info("Импорт завершён. Всего импортировано: {}", imported);

        } catch (Exception e) {
            logger.error("Ошибка при чтении файла", e);
        }
    }

    /**
     * Обработка отдельной строки файла.
     */
    private boolean processLine(PreparedStatement stmt, String line) {
        String[] parts = line.split("\\|\\|");

        if (parts.length < 5) {
            logger.warn("Пропущена некорректная строка: {}", line);
            return false;
        }

        try {
            stmt.setString(1, parts[0]);
            stmt.setString(2, parts[1]);
            stmt.setString(3, parts[2]);
            stmt.setInt(4, Integer.parseInt(parts[3]));
            stmt.setDouble(5, Double.parseDouble(parts[4].replace(",", ".")));

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            logger.error("Ошибка обработки строки: {}", line, e);
            return false;
        }
    }

    /**
     * Сброс таблицы перед импортом.
     */
    private void resetTable(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(DROP_TABLE_SQL);
            stmt.executeUpdate(CREATE_TABLE_SQL);

            logger.info("Таблица records очищена и создана заново.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сбросе таблицы", e);
        }
    }

    /**
     * Подсчёт количества строк в файле.
     */
    private int countLines(File file) {
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (Exception e) {
            logger.error("Ошибка при подсчёте строк файла", e);
        }

        return count;
    }
}
