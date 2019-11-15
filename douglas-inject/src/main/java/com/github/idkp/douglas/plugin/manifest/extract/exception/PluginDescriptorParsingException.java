package com.github.idkp.douglas.plugin.manifest.extract.exception;

public class PluginDescriptorParsingException extends RuntimeException {
    public PluginDescriptorParsingException(String message) {
        super(message);
    }

    public PluginDescriptorParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}