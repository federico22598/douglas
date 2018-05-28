package com.github.foskel.douglas.plugin.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public interface PluginRegistry {
    boolean register(PluginManifest manifest, Plugin plugin);

    boolean registerAll(Map<PluginManifest, Plugin> plugins);

    boolean unregister(PluginManifest manifest);

    boolean unregisterIf(Predicate<PluginManifest> condition);

    boolean unregisterAll(Collection<PluginManifest> manifests);

    PluginLocatorService getLocator();

    Map<PluginManifest, Plugin> findAllPlugins();

    void clear();
}