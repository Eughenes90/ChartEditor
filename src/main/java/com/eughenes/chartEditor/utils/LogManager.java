package com.eughenes.chartEditor.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager {

    private static final Logger logger = Logger.getLogger(LogManager.class.getName());

    // Configure the logger to use a single log file
    static {
        try {
            // Ensure the /logs/ directory exists
            File logsDirectory = new File("logs");
            if (!logsDirectory.exists()) {
                if (logsDirectory.mkdirs()) {
                    logger.info("Created logs directory: " + logsDirectory.getAbsolutePath());
                } else {
                    logger.warning("Failed to create logs directory.");
                }
            }

            // Create a single log file: application.log
            String logFilePath = "logs/application.log";
            FileHandler fileHandler = new FileHandler(logFilePath, true); // Append mode
            fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
            logger.addHandler(fileHandler);

            // Set the log level for the logger
            logger.setLevel(Level.INFO);

        } catch (IOException e) {
            logger.severe("Error setting up file handler for logging: " + e.getMessage());
        }
    }

    // Method to get the caller method name dynamically
    private static String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 4) {
            return stackTrace[3].getMethodName();
        } else {
            return "Unknown";
        }
    }

    // Log Info messages to the single log file with caller method info
    public static void logInfo(String message) {
        String methodName = getCallerMethodName();  // Get the caller method name dynamically
        logger.log(Level.INFO, "{0} - {1}", new Object[]{methodName, message});
    }

    // Log Warning messages to the single log file with caller method info
    public static void logWarning(String message) {
        String methodName = getCallerMethodName();  // Get the caller method name dynamically
        logger.log(Level.WARNING, "{0} - {1}", new Object[]{methodName, message});
    }

    // Log Error messages to the single log file with caller method info
    public static void logError(String message) {
        String methodName = getCallerMethodName();  // Get the caller method name dynamically
        logger.log(Level.SEVERE, "{0} - {1}", new Object[]{methodName, message});
    }

    // Log Error with exception to the single log file with caller method info
    public static void logError(String message, Throwable throwable) {
        String methodName = getCallerMethodName();  // Get the caller method name dynamically
        logger.log(Level.SEVERE, "{0} - {1}", new Object[]{methodName, message});
        logger.log(Level.SEVERE, throwable.getMessage(), throwable);
    }

}
