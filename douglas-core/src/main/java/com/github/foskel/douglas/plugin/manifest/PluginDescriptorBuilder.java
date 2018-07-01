package com.github.foskel.douglas.plugin.manifest;

import com.github.foskel.douglas.core.version.Version;

/**
 * @author Foskel
 */
public final class PluginDescriptorBuilder {
    private String groupId;
    private String artifactId;
    private Version version;
    private String name;

    public PluginDescriptorBuilder groupId(String groupId) {
        this.groupId = groupId;

        return this;
    }

    public PluginDescriptorBuilder artifactId(String artifactId) {
        this.artifactId = artifactId;

        return this;
    }

    public PluginDescriptorBuilder version(Version version) {
        this.version = version;

        return this;
    }

    public PluginDescriptorBuilder name(String name) {
        this.name = name;

        return this;
    }

    public PluginDescriptor build() {
        if (this.groupId == null
                || this.artifactId == null
                || this.version == null
                || this.name == null) {
            throw new IllegalStateException("You must finish building the PluginDescriptor " +
                    "before calling PluginDescriptorBuilder#build!");
        }

        return new BasePluginDescriptor(this.groupId,
                this.artifactId,
                this.version,
                this.name);
    }
}
