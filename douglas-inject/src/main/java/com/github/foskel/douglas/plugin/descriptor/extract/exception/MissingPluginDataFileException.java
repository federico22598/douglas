package com.github.foskel.douglas.plugin.descriptor.extract.exception;

public class MissingPluginDataFileException extends RuntimeException {
    public MissingPluginDataFileException(String message) {
        super(message);
    }
}