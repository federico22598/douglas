package com.github.foskel.douglas.plugin.impl.descriptor;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.descriptor.BaseArtifactDescriptor;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.util.Collection;
import java.util.Collections;

public final class SimplePluginDescriptor extends BaseArtifactDescriptor implements PluginDescriptor {
    private final String mainClassName;
    private final Collection<String> resourceTargets;
    private final Collection<PluginDescriptor> dependencyDescriptors;

    SimplePluginDescriptor(String mainClassName,
                           String groupId,
                           String artifactId,
                           Version version,
                           String name,
                           Collection<String> resourceTargets,
                           Collection<PluginDescriptor> dependencyDescriptors) {
        super(groupId,
                artifactId,
                version,
                name);

        this.mainClassName = mainClassName;
        this.resourceTargets = resourceTargets;
        this.dependencyDescriptors = dependencyDescriptors;
    }

    private static String identityToString(Object o) {
        return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
    }

    @Override
    public String getMainClass() {
        return this.mainClassName;
    }

    @Override
    public Collection<String> getResourceTargets() {
        return Collections.unmodifiableCollection(this.resourceTargets);
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

        if (!(object instanceof PluginDescriptor)) {
            return false;
        }

        PluginDescriptor other = (PluginDescriptor) object;

        return other.getGroupId().equals(this.groupId)
                && other.getArtifactId().equals(this.artifactId)
                && other.getVersion().equals(this.version)
                && other.getName().equals(this.name)
                && other.getMainClass().equals(this.mainClassName);
    }

    @Override
    public String toString() {
        return identityToString(this) + "{" +
                (this.groupId == null
                        ? "<null>"
                        : identityToString(this.groupId) + "[" + this.groupId + "]") + "," +
                (this.artifactId == null
                        ? "<null>"
                        : identityToString(this.artifactId) + "[" + this.artifactId + "]") + "," +
                (this.version == null
                        ? "<null>"
                        : identityToString(this.version) + "[" + this.version + "]") + "," +
                (this.name == null
                        ? "<null>"
                        : identityToString(this.name) + "[" + this.name + "]") + "," +
                (this.mainClassName == null
                        ? "<null>"
                        : identityToString(this.mainClassName) + "[" + this.mainClassName + "]") + "," +
                "}";
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.groupId.hashCode();
        hash = 31 * hash + this.artifactId.hashCode();
        hash = 31 * hash + this.version.hashCode();
        hash = 31 * hash + this.name.hashCode();
        hash = 31 * hash + this.mainClassName.hashCode();

        return hash;
    }
}