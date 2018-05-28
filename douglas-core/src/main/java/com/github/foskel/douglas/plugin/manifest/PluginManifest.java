package com.github.foskel.douglas.plugin.manifest;

import java.util.Collection;

public interface PluginManifest extends Comparable<PluginManifest> {
    String getMainClass();

    PluginDescriptor getDescriptor();

    Collection<String> getResources();

    Collection<PluginDescriptor> getDependencyDescriptors();

    @Override
    default int compareTo(PluginManifest o) {
        return this.getDescriptor().compareTo(o.getDescriptor());
    }

    static PluginManifestBuilder builder() {
        return new PluginManifestBuilder();
    }
}