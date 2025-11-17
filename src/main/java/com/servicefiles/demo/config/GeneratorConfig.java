package com.servicefiles.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorConfig {

    private int fileCount;
    private int linesPerFile ;
    private int latinLength;
    private int cyrillicLength;
    private int minInt;
    private int maxInt;
    private double minFloat;
    private double maxFloat;
    private int daysBack;
    private String outputDir;
    private String separator;

}
