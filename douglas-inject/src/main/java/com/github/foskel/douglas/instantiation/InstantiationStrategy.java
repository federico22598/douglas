package com.github.foskel.douglas.instantiation;

/**
 * @author Foskel
 */
public interface InstantiationStrategy<T> {
    T instantiate(Class<? extends T> type, ClassLoader classLoader) throws InstantiationException;
}