package com.github.foskel.douglas.plugin.impl.descriptor;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class PluginDescriptorBuilder {
    private final Set<PluginDescriptor> dependencyDescriptors;
    private final Set<String> resourceTargets;
    private String groupId;
    private String artifactId;
    private Version version;
    private String name;
    private String mainClassName;

    public PluginDescriptorBuilder() {
        this.dependencyDescriptors = new HashSet<>();
        this.resourceTargets = new HashSet<>();
    }

    public PluginDescriptorBuilder addDependencyDescriptor(PluginDescriptor dependencyDescriptor) {
        this.dependencyDescriptors.add(dependencyDescriptor);

        return this;
    }

    public PluginDescriptorBuilder addDependencyDescriptors(Collection<PluginDescriptor> dependencyDescriptor) {
        this.dependencyDescriptors.addAll(dependencyDescriptor);

        return this;
    }

    public PluginDescriptorBuilder addResourceTargets(Collection<String> resourceTargets) {
        this.resourceTargets.addAll(resourceTargets);

        return this;
    }

    public PluginDescriptorBuilder withGroupId(String groupId) {
        this.groupId = groupId;

        return this;
    }

    public PluginDescriptorBuilder withArtifactId(String artifactId) {
        this.artifactId = artifactId;

        return this;
    }

    public PluginDescriptorBuilder withVersion(Version version) {
        this.version = version;

        return this;
    }

    public PluginDescriptorBuilder withName(String name) {
        this.name = name;

        return this;
    }

    public PluginDescriptorBuilder withClassName(String className) {
        this.mainClassName = className;

        return this;
    }

    public PluginDescriptor build() {
        if (this.groupId == null
                || this.artifactId == null
                || this.version == null
                || this.name == null) {
            throw new IllegalStateException("You must finish building the SimplePluginDescriptor " +
                    "before calling PluginDescriptorBuilder#build!");
        }

        return this.mainClassName == null
                ? this.dependency()
                : this.simple();
    }

    private PluginDescriptor dependency() {
        return DependencyPluginDescriptorDecorator.of(this.groupId,
                this.artifactId,
                this.version,
                this.name,
                this.resourceTargets,
                this.dependencyDescriptors);
    }

    private PluginDescriptor simple() {
        return new SimplePluginDescriptor(this.mainClassName,
                this.groupId,
                this.artifactId,
                this.version,
                this.name,
                this.resourceTargets,
                this.dependencyDescriptors);
    }
}