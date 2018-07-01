package com.github.foskel.douglas.plugin.manifest.extract.exception;

/**
 * @author Foskel
 */
public class PluginDescriptorParsingException extends RuntimeException {
    public PluginDescriptorParsingException(String message) {
        super(message);
    }

    public PluginDescriptorParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}