package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.scan.PluginScanWorker;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.List;

/**
 * @author Foskel
 */
public interface PluginScanResult {
    PluginManifest getManifest();

    Plugin getPlugin();

    List<PluginDescriptor> getPendingDependencyDescriptors();

    PluginScanWorker getScanWorker();
}