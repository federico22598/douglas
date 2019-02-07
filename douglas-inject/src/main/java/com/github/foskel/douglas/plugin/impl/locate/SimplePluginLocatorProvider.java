package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Foskel
 */
public final class SimplePluginLocatorProvider implements PluginLocatorProvider {

    @Override
    public PluginLocatorService createPluginLocator(Map<PluginDescriptor, Plugin> plugins) {
        return new SimplePluginLocator(plugins);
    }

    @Override
    public PluginLocatorService createPluginLocatorFromManifests(Map<PluginManifest, Plugin> plugins) {
        HashMap<PluginDescriptor, Plugin> descriptorsMap = new HashMap<>();

        for (Map.Entry<PluginManifest, Plugin> pluginEntry : plugins.entrySet()) {
            descriptorsMap.put(pluginEntry.getKey().getDescriptor(), pluginEntry.getValue());
        }

        return new SimplePluginLocator(descriptorsMap);
    }
}
