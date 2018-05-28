package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface PluginLocatorService {
    Optional<Plugin> find(String groupId,
                          String artifactId,
                          String version,
                          String name);

    Optional<Plugin> find(PluginDescriptor descriptor);

    Optional<Plugin> find(PluginManifest manifest);

    Set<Plugin> findAll(Predicate<PluginManifest> condition);
}