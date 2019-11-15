package com.github.idkp.douglas.plugin.manifest;

import com.github.idkp.douglas.core.descriptor.ArtifactDescriptor;
import com.github.idkp.douglas.core.traits.Named;
import com.github.idkp.douglas.core.traits.Versioned;
import com.github.idkp.douglas.core.version.Version;

public interface PluginDescriptor extends Comparable<PluginDescriptor>, Named, Versioned, ArtifactDescriptor {

    static PluginDescriptorBuilder builder() {
        return new PluginDescriptorBuilder();
    }

    @Override
    String getGroupId();

    @Override
    String getArtifactId();

    @Override
    Version getVersion();

    @Override
    String getName();

    @Override
    default int compareTo(PluginDescriptor descriptor) {
        return this.getVersion().compareTo(descriptor.getVersion());
    }
}