package com.servicefiles.demo.contoller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.servicefiles.demo.config.GeneratorConfig;
import com.servicefiles.demo.service.FileGenerationService;
import com.servicefiles.demo.service.ImporterService;
import com.servicefiles.demo.service.MergerService;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileGenerationService fileGenerationService;
    private final MergerService mergerService;
    private final ImporterService importerService;
    private final GeneratorConfig config;

    public FileController(FileGenerationService fileGenerationService,
                          MergerService mergerService,
                          ImporterService importerService,
                          GeneratorConfig config) {
        this.fileGenerationService = fileGenerationService;
        this.mergerService = mergerService;
        this.importerService = importerService;
        this.config = config;
    }

    @GetMapping("/")
    public ResponseEntity<String> index() {
        fileGenerationService.generateFiles();
        return ResponseEntity.ok("Hello api/files - /generate /import /merge");
    }
    
    @PostMapping("/generate")
    public ResponseEntity<String> generateFiles() {
        fileGenerationService.generateFiles();
        return ResponseEntity.ok("Файлы сгенерированы успешно.");
    }

    @PostMapping("/merge")
    public ResponseEntity<String> mergeFiles(
            @RequestParam(required = false) String remove) {

        File outputDir = new File(config.getOutputDir());
        if (!outputDir.exists()) {
            return ResponseEntity.badRequest().body("Папка с файлами не найдена.");
        }

        File mergedFile = new File("merged.txt");

        try {
            mergerService.mergeFiles(outputDir, mergedFile, remove);
        } catch (Exception e) {
            logger.error("Ошибка при объединении файлов", e);
            return ResponseEntity.internalServerError().body("Ошибка при объединении файлов.");
        }

        return ResponseEntity.ok("Файл объединён успешно.");
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFile(
            @RequestParam(required = true) String fileName) {

        if (fileName == null || fileName.isBlank()) {
            return ResponseEntity.badRequest().body("Имя файла не указано.");
        }

        File file = new File(config.getOutputDir(), fileName);

        if (!file.exists()) {
            return ResponseEntity.status(404)
                    .body("Файл не найден: " + file.getAbsolutePath());
        }

        try {
            importerService.importFile(file);
        } catch (Exception e) {
            logger.error("Ошибка импорта файла", e);
            return ResponseEntity.internalServerError().body("Ошибка при импорте файла.");
        }

        return ResponseEntity.ok("Файл импортирован успешно.");
    }
}
