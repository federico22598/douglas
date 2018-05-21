package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface PluginLocatorService {
    Optional<Plugin> find(PluginDescriptor descriptor);

    Optional<Plugin> find(Predicate<PluginDescriptor> condition);

    Set<Plugin> findAll(Predicate<Plugin> condition);

    Map<PluginDescriptor, Plugin> findAllPlugins();
}