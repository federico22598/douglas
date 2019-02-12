package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Foskel
 */
public final class SimplePluginLocator implements PluginLocatorService {
    //private static final String LATEST_VERSION = "<latest>";

    private final Map<PluginDescriptor, Plugin> plugins;

    public SimplePluginLocator(Map<PluginDescriptor, Plugin> plugins) {
        this.plugins = plugins;
    }

    public static SimplePluginLocator fromManifests(Map<PluginManifest, Plugin> plugins) {
        HashMap<PluginDescriptor, Plugin> descriptorsMap = new HashMap<>();

        for (Map.Entry<PluginManifest, Plugin> pluginEntry : plugins.entrySet()) {
            descriptorsMap.put(pluginEntry.getKey().getDescriptor(), pluginEntry.getValue());
        }

        return new SimplePluginLocator(descriptorsMap);
    }

    @Override
    public Plugin find(PluginDescriptor descriptor) {
        return this.plugins.get(descriptor);
    }

    @Override
    public Plugin find(String groupId, String artifactId) {
        for (Map.Entry<PluginDescriptor, Plugin> entry : this.plugins.entrySet()) {
            PluginDescriptor descriptor = entry.getKey();

            if (descriptor.getGroupId().equals(groupId) && descriptor.getArtifactId().equals(artifactId)) {
                return entry.getValue();
            }
        }

        return null;
    }

    /*@Override
    public Optional<Plugin> find(String groupId, String artifactId, String version, String name) {
        PluginDescriptor previousDescriptor = null;
        Plugin result = null;

        for (Map.Entry<PluginDescriptor, Plugin> entry : this.plugins.entrySet()) {
            PluginDescriptor descriptor = entry.getKey();

            if (matches(descriptor, groupId, artifactId, name)) {
                if (version == null || version.equals(LATEST_VERSION)) {
                    // use the latest plugin

                    if (previousDescriptor != null && previousDescriptor.compareTo(descriptor) < 0) {
                        result = entry.getValue();
                    }
                } else if (version.equals(descriptor.getVersion().toString())) {
                    result = entry.getValue();
                }
            }

            previousDescriptor = descriptor;
        }

        return Optional.ofNullable(result);
    }*/

    @Override
    public Set<Plugin> findAll(Predicate<PluginDescriptor> condition) {
        Set<Plugin> result = new HashSet<>();

        for (Map.Entry<PluginDescriptor, Plugin> pluginEntry : this.plugins.entrySet()) {
            if (condition.test(pluginEntry.getKey())) {
                result.add(pluginEntry.getValue());
            }
        }

        return result;
    }
}