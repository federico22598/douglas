package com.github.foskel.douglas.plugin.descriptor.extract.exception;

public class MissingPluginDataContentException extends RuntimeException {
    public MissingPluginDataContentException(String message) {
        super(message);
    }

    public MissingPluginDataContentException(String message,
                                             Throwable cause) {
        super(message, cause);
    }
}