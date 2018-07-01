package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

/**
 * @author Foskel
 */
public interface PluginScanResult {
    PluginManifest getDescriptor();

    Plugin getPlugin();
}