package com.github.foskel.douglas.plugin.descriptor;

import com.github.foskel.douglas.core.descriptor.Descriptor;
import com.github.foskel.douglas.core.version.Version;

import java.util.Collection;

public interface PluginDescriptor extends Comparable<PluginDescriptor>, Descriptor {
    String getMainClass();

    @Override
    String getGroupId();

    @Override
    String getArtifactId();

    @Override
    Version getVersion();

    @Override
    String getName();

    Collection<String> getResourceTargets();

    Collection<PluginDescriptor> getDependencyDescriptors();

    @Override
    default int compareTo(PluginDescriptor o) {
        return this.getVersion().compareTo(o.getVersion());
    }
}