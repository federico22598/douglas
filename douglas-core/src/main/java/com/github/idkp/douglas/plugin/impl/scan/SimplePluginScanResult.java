package com.github.idkp.douglas.plugin.impl.scan;

import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.douglas.plugin.manifest.PluginManifest;
import com.github.idkp.douglas.plugin.scan.PluginScanResult;
import com.github.idkp.douglas.util.ToStringBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SimplePluginScanResult implements PluginScanResult {
    private final PluginManifest manifest;
    private final Plugin plugin;
    private final List<PluginDescriptor> pendingDependencyDescriptors;
    private final PluginScanWorker scanWorker;

    private SimplePluginScanResult(PluginManifest manifest, Plugin plugin, List<PluginDescriptor> pendingDependencyDescriptors,
                                   PluginScanWorker scanWorker) {
        this.manifest = manifest;
        this.plugin = plugin;
        this.pendingDependencyDescriptors = pendingDependencyDescriptors;
        this.scanWorker = scanWorker;
    }

    public SimplePluginScanResult(PluginManifest manifest, Plugin plugin, PluginScanWorker scanWorker) {
        this(Objects.requireNonNull(manifest, "manifest"), Objects.requireNonNull(plugin, "plugin"),
                Collections.emptyList(), scanWorker);
    }

    public SimplePluginScanResult(PluginManifest manifest, List<PluginDescriptor> pendingDependencyDescriptors, PluginScanWorker scanWorker) {
        this(manifest, null, pendingDependencyDescriptors, scanWorker);
    }

    @Override
    public PluginManifest getManifest() {
        return this.manifest;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public List<PluginDescriptor> getPendingDependencyDescriptors() {
        return this.pendingDependencyDescriptors;
    }

    @Override
    public PluginScanWorker getScanWorker() {
        return this.scanWorker;
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

        return Objects.equals(other.getManifest(), this.manifest)
                && Objects.equals(other.getPlugin(), this.plugin);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .attribute(this.plugin)
                .attribute(this.manifest)
                .build();
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.manifest.hashCode();
        hash = 31 * hash + this.plugin.hashCode();

        return hash;
    }
}
