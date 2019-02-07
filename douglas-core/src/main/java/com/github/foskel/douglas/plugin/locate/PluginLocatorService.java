package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Foskel
 */
public interface PluginLocatorService {
    Optional<Plugin> find(String groupId,
                          String artifactId,
                          String version,
                          String name);

    Optional<Plugin> find(PluginDescriptor descriptor);

    Set<Plugin> findAll(Predicate<PluginDescriptor> condition);
}