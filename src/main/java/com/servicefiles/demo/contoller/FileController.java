package com.servicefiles.demo.contoller;

import java.io.File;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servicefiles.demo.config.GeneratorConfig;
import com.servicefiles.demo.service.FileGenerationService;
import com.servicefiles.demo.service.ImporterService;
import com.servicefiles.demo.service.MergerService;

@RestController
@RequestMapping("/api/files")
public class FileController {

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

    @PostMapping("/generate")
    public ResponseEntity<String> generateFiles() {
        fileGenerationService.generateFiles();
        return new ResponseEntity("Файлы сгеренированы успешно",HttpStatusCode.valueOf(200));
    }

    @PostMapping("/merge")
    public ResponseEntity mergeFiles(@RequestParam(required = false) String remove) {
        File mergedFile = new File("merged.txt");
        mergerService.mergeFiles(new File(config.getOutputDir()), mergedFile, remove);
        return new ResponseEntity("Файл объединен успешно",HttpStatusCode.valueOf(200));
    }

    @PostMapping("/import")
    public ResponseEntity importFiles(@RequestParam(required = false) String fileName) {
        importerService.importFile(new File(config.getOutputDir() +"/"+ fileName));
        return new ResponseEntity("Файл импортирован в БД успешно",HttpStatusCode.valueOf(200));
    }
}
