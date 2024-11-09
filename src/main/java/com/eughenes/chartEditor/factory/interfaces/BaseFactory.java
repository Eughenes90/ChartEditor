package com.eughenes.chartEditor.factory.interfaces;

import java.io.IOException;

/**
 * Generic Interface for factory classes
 *
 * @author Eughenes
 */
public interface BaseFactory<T, I> {
    T create(I input) throws IOException;
}
