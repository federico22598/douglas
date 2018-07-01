package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.plugin.registry.PluginRegistry;

import java.nio.file.Path;

/**
 * This class should be used to handle basic loading and unloading of plugins.
 * It also has a {@link PluginRegistry} to register, unregister and find plugins directly.
 *
 * @author Foskel
 */
public interface PluginManager {
    /**
     * Registers the plugins found in the specified {@code pluginsDirectory} directory into the registry
     * and loads them.
     *
     * @param pluginsDirectory the directory where the plugin files should be inside
     */
    void load(Path pluginsDirectory);

    /**
     * Loads a single plugin contained in the specified {@code pluginFile} file.
     * If no plugin is found, nothing will happen.
     *
     * @param pluginFile a file containing a single plugin
     */
    void loadSingle(Path pluginFile);

    /**
     * Unloads all the plugins registered.
     */
    void unload();

    /**
     * Returns a {@link PluginRegistry} containing all the loaded plugins.
     *
     * @return a {@link PluginRegistry} containing all the loaded plugins
     */
    PluginRegistry getRegistry();
}