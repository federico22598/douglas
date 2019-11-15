package com.github.idkp.douglas.plugin.scan;

public class PluginScanFailedException extends RuntimeException {
    public PluginScanFailedException(String message) {
        super(message);
    }

    public PluginScanFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
