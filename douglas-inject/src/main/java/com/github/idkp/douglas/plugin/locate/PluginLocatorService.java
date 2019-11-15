package com.github.idkp.douglas.plugin.locate;

import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;

import java.util.Set;
import java.util.function.Predicate;

public interface PluginLocatorService {
    Plugin find(PluginDescriptor descriptor);

    Plugin find(String groupId, String artifactId);

    Set<Plugin> findAll(Predicate<PluginDescriptor> condition);
}