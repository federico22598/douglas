package com.github.foskel.douglas.plugin.descriptor.extract.exception;

public class PluginDescriptorParsingException extends RuntimeException {
    public PluginDescriptorParsingException(String message) {
        super(message);
    }

    public PluginDescriptorParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}