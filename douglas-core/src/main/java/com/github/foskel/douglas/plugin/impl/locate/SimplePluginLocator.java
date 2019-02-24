package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Foskel
 */
public final class SimplePluginLocator implements PluginLocatorService {
    //private static final String LATEST_VERSION = "<latest>";

    //private final Map<PluginDescriptor, Plugin> plugins;
    private final PluginRegistry registry;

    public SimplePluginLocator(PluginRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Plugin find(PluginDescriptor descriptor) {
        Map<PluginManifest, Plugin> plugins = registry.findAllPlugins();

        for (Map.Entry<PluginManifest, Plugin> pluginEntry : plugins.entrySet()) {
            if (descriptor.equals(pluginEntry.getKey().getDescriptor())) {
                return pluginEntry.getValue();
            }
        }

        return null;
    }

    @Override
    public Plugin find(String groupId, String artifactId) {
        Map<PluginManifest, Plugin> plugins = registry.findAllPlugins();

        for (Map.Entry<PluginManifest, Plugin> pluginEntry : plugins.entrySet()) {
            PluginDescriptor descriptor = pluginEntry.getKey().getDescriptor();

            if (descriptor.getGroupId().equals(groupId) && descriptor.getArtifactId().equals(artifactId)) {
                return pluginEntry.getValue();
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
        Map<PluginManifest, Plugin> plugins = registry.findAllPlugins();

        for (Map.Entry<PluginManifest, Plugin> pluginEntry : plugins.entrySet()) {
            if (condition.test(pluginEntry.getKey().getDescriptor())) {
                result.add(pluginEntry.getValue());
            }
        }

        return result;
    }
}