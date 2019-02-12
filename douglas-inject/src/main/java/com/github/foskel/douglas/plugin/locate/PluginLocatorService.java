package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;

import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Foskel
 */
public interface PluginLocatorService {
    Plugin find(PluginDescriptor descriptor);

    Plugin find(String groupId, String artifactId);

    Set<Plugin> findAll(Predicate<PluginDescriptor> condition);
}