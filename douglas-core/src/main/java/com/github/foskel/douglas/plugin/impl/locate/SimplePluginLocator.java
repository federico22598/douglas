package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SimplePluginLocator implements PluginLocatorService {
    private static final String LATEST_VERSION = "<latest>";

    private final Map<PluginManifest, Plugin> plugins;

    public SimplePluginLocator(Map<PluginManifest, Plugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public Optional<Plugin> find(PluginDescriptor descriptor) {
        Plugin result = null;

        for (Map.Entry<PluginManifest, Plugin> entry : this.plugins.entrySet()) {
            if (entry.getKey().getDescriptor() == descriptor) {
                result = entry.getValue();
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Plugin> find(PluginManifest manifest) {
        return Optional.ofNullable(this.plugins.get(manifest));
    }

    @Override
    public Optional<Plugin> find(String groupId, String artifactId, String version, String name) {
        PluginDescriptor previousDescriptor = null;
        Plugin result = null;

        for (Map.Entry<PluginManifest, Plugin> entry : this.plugins.entrySet()) {
            PluginDescriptor descriptor = entry.getKey().getDescriptor();

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
    }

    private static boolean matches(PluginDescriptor descriptor,
                                   String groupId,
                                   String artifactId,
                                   String name) {
        return descriptor.getGroupId().equals(groupId)
                && descriptor.getArtifactId().equals(artifactId)
                && descriptor.getName().equals(name);
    }

    @Override
    public Set<Plugin> findAll(Predicate<PluginManifest> condition) {
        return this.plugins.entrySet()
                .stream()
                .filter(entry -> condition.test(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}