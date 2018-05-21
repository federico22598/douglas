package com.github.foskel.douglas.plugin.resource;

public interface ResourceHandler {
    void handle(Class<?> type, ClassLoader classLoader);
}