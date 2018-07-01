package com.github.foskel.douglas.plugin.scan;

import java.nio.file.Path;
import java.util.Collection;

/**
 * @author Foskel
 */
public interface PluginScanningStrategy {
    static PluginScanningStrategyBuilder builder() {
        return new PluginScanningStrategyBuilder();
    }

    Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException;

    PluginScanResult scanSingle(Path file);
}