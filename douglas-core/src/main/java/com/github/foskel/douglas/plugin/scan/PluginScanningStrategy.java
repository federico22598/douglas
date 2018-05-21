package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.plugin.impl.scan.PluginScanningStrategyBuilder;

import java.nio.file.Path;
import java.util.Collection;

public interface PluginScanningStrategy {
    static PluginScanningStrategyBuilder builder() {
        return new PluginScanningStrategyBuilder();
    }

    Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException;
}