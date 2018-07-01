package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;

import java.util.Objects;

/**
 * @author Foskel
 */
public final class SimplePluginScanResult implements PluginScanResult {
    private final PluginManifest descriptor;
    private final Plugin plugin;

    public SimplePluginScanResult(PluginManifest descriptor, Plugin plugin) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor");
        this.plugin = Objects.requireNonNull(plugin, "plugin");
    }

    private static String identityToString(Object o) {
        return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
    }

    @Override
    public PluginManifest getDescriptor() {
        return this.descriptor;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof PluginScanResult)) {
            return false;
        }

        PluginScanResult other = (PluginScanResult) object;

        return Objects.equals(other.getDescriptor(), this.descriptor)
                && Objects.equals(other.getPlugin(), this.plugin);
    }

    @Override
    public String toString() {
        return identityToString(this) + "{" +
                (this.plugin == null
                        ? "<null>"
                        : identityToString(this.plugin) + "[" + this.plugin + "]") + "," +
                (this.descriptor == null
                        ? "<null>"
                        : identityToString(this.descriptor) + "[" + this.descriptor + "]") +
                "}";
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.descriptor.hashCode();
        hash = 31 * hash + this.plugin.hashCode();

        return hash;
    }
}
