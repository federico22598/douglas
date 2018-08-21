package com.github.foskel.douglas.plugin.manifest;

import java.util.Collection;

/**
 * @author Foskel
 */
public interface PluginManifest extends Comparable<PluginManifest> {
    static PluginManifestBuilder builder() {
        return new PluginManifestBuilder();
    }

    String getMainClass();

    PluginDescriptor getDescriptor();

    Collection<String> getResources();

    Collection<PluginDescriptor> getDependencyDescriptors();

    @Override
    default int compareTo(PluginManifest o) {
        return this.getDescriptor().compareTo(o.getDescriptor());
    }
}