package com.eughenes.chartEditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point — starts embedded Tomcat and serves the web UI
 *
 * @author Eughenes
 */
@SpringBootApplication
public class ChartEditor {
    public static void main(String[] args) {
        SpringApplication.run(ChartEditor.class, args);
    }
}
