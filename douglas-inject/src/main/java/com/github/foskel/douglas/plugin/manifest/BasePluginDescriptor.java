package com.github.foskel.douglas.plugin.manifest;

import com.github.foskel.douglas.core.version.Version;

/**
 * @author Foskel
 */
public final class BasePluginDescriptor implements PluginDescriptor {
    protected final String groupId;
    protected final String artifactId;
    protected final Version version;
    protected final String name;

    public BasePluginDescriptor(String groupId,
                                String artifactId,
                                Version version,
                                String name) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.name = name;
    }

    private static String identityToString(Object o) {
        return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
    }

    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public String getArtifactId() {
        return this.artifactId;
    }

    @Override
    public Version getVersion() {
        return this.version;
    }

    @Override
    public String getName() {
        return this.name;
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
                && (other.getVersion() == null || other.getVersion().equals(this.version))
                && (other.getName() == null || other.getName().equals(this.name));
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
                        : identityToString(this.name) + "[" + this.name + "]") +
                "}";
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.groupId.hashCode();
        hash = 31 * hash + this.artifactId.hashCode();

        if (this.version != null) {
            hash = 31 * hash + this.version.hashCode();
        }

        if (this.name != null) {
            hash = 31 * hash + this.name.hashCode();
        }

        return hash;
    }
}