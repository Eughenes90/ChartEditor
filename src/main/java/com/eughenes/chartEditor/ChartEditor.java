package com.eughenes.chartEditor;

import com.eughenes.chartEditor.config.FileTransformerConfig;
import com.eughenes.chartEditor.service.ChartService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Main method to start the application
 *
 * @author Eughenes
 */
public class ChartEditor {
    public static void main(String[] args) throws IOException {
        // Initialize Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(FileTransformerConfig.class);

        // Retrieve ChartService bean from context
        ChartService chartService = context.getBean(ChartService.class);

        // Use ChartService to proceed to execution
        chartService.processChart();
    }
}
