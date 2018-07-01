package com.github.foskel.douglas.plugin.impl.manifest;

import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.util.ToStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Foskel
 */
public final class SimplePluginManifest implements PluginManifest {
    private final String mainClass;
    private final PluginDescriptor descriptor;
    private final Collection<String> resources;
    private final Collection<PluginDescriptor> dependencyDescriptors;

    public SimplePluginManifest(String mainClass,
                                PluginDescriptor descriptor,
                                Collection<String> resources,
                                Collection<PluginDescriptor> dependencyDescriptors) {
        this.mainClass = mainClass;
        this.descriptor = descriptor;
        this.resources = resources;
        this.dependencyDescriptors = dependencyDescriptors;
    }

    @Override
    public String getMainClass() {
        return this.mainClass;
    }

    @Override
    public PluginDescriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    public Collection<String> getResources() {
        return Collections.unmodifiableCollection(this.resources);
    }

    @Override
    public Collection<PluginDescriptor> getDependencyDescriptors() {
        return Collections.unmodifiableCollection(this.dependencyDescriptors);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof PluginManifest)) {
            return false;
        }

        PluginManifest other = (PluginManifest) object;

        return other.getMainClass().equals(this.mainClass)
                && other.getDescriptor().equals(this.descriptor)
                && other.getResources().equals(this.resources)
                && other.getDependencyDescriptors().equals(this.dependencyDescriptors);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .attribute(this.mainClass)
                .attribute(this.descriptor)
                .attribute(this.resources)
                .attribute(this.dependencyDescriptors)
                .build();
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[]{this.mainClass,
                this.descriptor,
                this.resources,
                this.dependencyDescriptors});
    }
}