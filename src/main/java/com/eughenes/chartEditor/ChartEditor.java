package com.eughenes.chartEditor;

import com.eughenes.chartEditor.config.ChartEditorConfig;
import com.eughenes.chartEditor.view.ui.MainPanel;
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
        ApplicationContext context = new AnnotationConfigApplicationContext(ChartEditorConfig.class);

        // Retrieve ChartService bean from context
        MainPanel chartService = context.getBean(MainPanel.class);

        // Use ChartService to proceed to execution
        chartService.createUIComponents();
    }
}
