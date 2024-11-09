package com.eughenes.chartEditor.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base component used to add logging functionalities to all the other classes
 *
 * @author Eughenes
 */
public class BaseComponent {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

}
