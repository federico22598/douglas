package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.plugin.impl.scan.PluginScanWorker;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

public final class UnloadedPluginDependencyData {
    public final PluginManifest manifest;
    public final PluginScanWorker scanWorker;

    public UnloadedPluginDependencyData(PluginManifest manifest, PluginScanWorker scanWorker) {
        this.manifest = manifest;
        this.scanWorker = scanWorker;
    }
}
