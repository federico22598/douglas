package com.github.idkp.douglas.plugin.scan;

import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.impl.scan.PluginScanWorker;
import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.douglas.plugin.manifest.PluginManifest;

import java.util.List;

public interface PluginScanResult {
    PluginManifest getManifest();

    Plugin getPlugin();

    List<PluginDescriptor> getPendingDependencyDescriptors();

    PluginScanWorker getScanWorker();
}