package com.github.idkp.douglas.plugin.resource;

public interface ResourceHandler {
    void handle(Class<?> type, ClassLoader classLoader);
}