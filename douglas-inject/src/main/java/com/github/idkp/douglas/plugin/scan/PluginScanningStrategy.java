package com.github.idkp.douglas.plugin.scan;

import java.nio.file.Path;
import java.util.Collection;

public interface PluginScanningStrategy {
    Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException;

    PluginScanResult scanSingle(Path file);
}