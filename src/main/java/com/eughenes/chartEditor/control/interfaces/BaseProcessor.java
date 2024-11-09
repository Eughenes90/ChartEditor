package com.eughenes.chartEditor.control.interfaces;

/**
 * Generic Interface for processors
 *
 * @author Eughenes
 */
public interface BaseProcessor<T> {
    T process(T element, Object... params);
}
