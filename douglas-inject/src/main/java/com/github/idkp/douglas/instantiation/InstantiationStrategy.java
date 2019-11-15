package com.github.idkp.douglas.instantiation;

public interface InstantiationStrategy<T> {
    T instantiate(Class<? extends T> type, ClassLoader classLoader) throws InstantiationException;
}