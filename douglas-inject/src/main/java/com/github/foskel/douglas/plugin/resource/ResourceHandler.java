package com.github.foskel.douglas.plugin.resource;

/**
 * @author Foskel
 */
public interface ResourceHandler {
    void handle(Class<?> type, ClassLoader classLoader);
}