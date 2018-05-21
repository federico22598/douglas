package com.github.foskel.douglas.plugin.impl.dependency.scan.result;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.haptor.scan.DependencyScanResult;

public final class NullableDependencyScanResult implements DependencyScanResult<PluginDescriptor, Plugin> {
    private final PluginDescriptor descriptor;

    public NullableDependencyScanResult(PluginDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public PluginDescriptor getDependencyIdentifier() {
        return this.descriptor;
    }

    @Override
    public Plugin getDependency() {
        return null;//Dependency is null till it's satisfied
    }
}