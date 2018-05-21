package com.github.foskel.douglas.plugin.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public interface PluginRegistry {
    boolean register(PluginDescriptor descriptor,
                     Plugin plugin);

    boolean registerAll(Map<PluginDescriptor, Plugin> plugins);

    boolean unregister(PluginDescriptor descriptor);

    boolean unregisterIf(Predicate<PluginDescriptor> condition);

    boolean unregisterAll(Collection<PluginDescriptor> pluginInformationEntries);

    PluginLocatorService getLocator();

    Map<PluginDescriptor, Plugin> findAllPlugins();

    void clear();
}