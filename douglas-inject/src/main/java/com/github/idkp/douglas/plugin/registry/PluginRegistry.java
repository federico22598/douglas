package com.github.idkp.douglas.plugin.registry;

import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.locate.PluginLocatorService;
import com.github.idkp.douglas.plugin.manifest.PluginManifest;

import java.util.Map;
import java.util.function.Predicate;

public interface PluginRegistry {
    boolean register(PluginManifest manifest, Plugin plugin);

    boolean registerAll(Map<PluginManifest, Plugin> plugins);

    boolean unregister(PluginManifest manifest);

    boolean unregisterIf(Predicate<PluginManifest> condition);

    PluginLocatorService getLocator();

    Map<PluginManifest, Plugin> findAllPlugins();

    void clear();
}