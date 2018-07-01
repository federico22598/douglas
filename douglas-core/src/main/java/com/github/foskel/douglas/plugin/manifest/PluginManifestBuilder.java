package com.github.foskel.douglas.plugin.manifest;

import com.github.foskel.douglas.plugin.impl.manifest.SimplePluginManifest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Foskel
 */
public final class PluginManifestBuilder {
    private final Set<PluginDescriptor> dependencyDescriptors;
    private final Set<String> resourceTargets;
    private String mainClassName;
    private PluginDescriptor descriptor;

    public PluginManifestBuilder() {
        this.dependencyDescriptors = new HashSet<>();
        this.resourceTargets = new HashSet<>();
    }

    public PluginManifestBuilder addDependencyDescriptor(PluginDescriptor dependencyDescriptor) {
        this.dependencyDescriptors.add(dependencyDescriptor);

        return this;
    }

    public PluginManifestBuilder addDependencyDescriptors(Collection<PluginDescriptor> dependencyDescriptor) {
        this.dependencyDescriptors.addAll(dependencyDescriptor);

        return this;
    }

    public PluginManifestBuilder addResourceTargets(Collection<String> resourceTargets) {
        this.resourceTargets.addAll(resourceTargets);

        return this;
    }


    public PluginManifestBuilder withClassName(String className) {
        this.mainClassName = className;

        return this;
    }

    public PluginManifestBuilder withDescriptor(PluginDescriptor descriptor) {
        this.descriptor = descriptor;

        return this;
    }

    public PluginManifest build() {
        if (this.mainClassName == null || this.descriptor == null) {
            throw new IllegalStateException("You must finish building the PluginManifest" +
                    "before calling PluginManifestBuilder#build!");
        }

        return new SimplePluginManifest(this.mainClassName,
                this.descriptor,
                this.resourceTargets,
                this.dependencyDescriptors);
    }
}