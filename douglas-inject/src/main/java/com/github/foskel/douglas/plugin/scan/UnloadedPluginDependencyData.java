package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.plugin.impl.scan.PluginScanWorker;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.Iterator;

public final class UnloadedPluginDependencyData {
    public final PluginManifest manifest;
    public final PluginScanWorker scanWorker;
    public final Iterator<PluginDescriptor> dependencyDescriptorIterator;

    public UnloadedPluginDependencyData(PluginManifest manifest, PluginScanWorker scanWorker, Iterator<PluginDescriptor> dependencyDescriptorIterator) {
        this.manifest = manifest;
        this.scanWorker = scanWorker;
        this.dependencyDescriptorIterator = dependencyDescriptorIterator;
    }
}
