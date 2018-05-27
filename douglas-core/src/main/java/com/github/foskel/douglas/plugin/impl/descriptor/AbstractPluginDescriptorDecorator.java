package com.github.foskel.douglas.plugin.impl.descriptor;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.util.Collection;

public abstract class AbstractPluginDescriptorDecorator implements PluginDescriptor {
    private final PluginDescriptor backingDecoratedInformationEntry;

    AbstractPluginDescriptorDecorator(PluginDescriptor backingDecoratedInformationEntry) {
        this.backingDecoratedInformationEntry = backingDecoratedInformationEntry;
    }

    @Override
    public String getMainClass() {
        return this.backingDecoratedInformationEntry.getMainClass();
    }

    @Override
    public String getGroupId() {
        return this.backingDecoratedInformationEntry.getGroupId();
    }

    @Override
    public String getArtifactId() {
        return this.backingDecoratedInformationEntry.getArtifactId();
    }

    @Override
    public Version getVersion() {
        return this.backingDecoratedInformationEntry.getVersion();
    }

    @Override
    public String getName() {
        return this.backingDecoratedInformationEntry.getName();
    }

    @Override
    public Collection<String> getResourceTargets() {
        return this.backingDecoratedInformationEntry.getResourceTargets();
    }

    @Override
    public Collection<PluginDescriptor> getDependencyDescriptors() {
        return this.backingDecoratedInformationEntry.getDependencyDescriptors();
    }

    @Override
    public int compareTo(PluginDescriptor o) {
        return this.backingDecoratedInformationEntry.compareTo(o);
    }
}