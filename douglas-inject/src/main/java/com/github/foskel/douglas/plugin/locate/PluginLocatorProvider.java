package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.Map;

/**
 * @author Foskel
 */
public interface PluginLocatorProvider {
    PluginLocatorService createPluginLocator(Map<PluginDescriptor, Plugin> plugins);

    PluginLocatorService createPluginLocatorFromManifests(Map<PluginManifest, Plugin> plugins);
}