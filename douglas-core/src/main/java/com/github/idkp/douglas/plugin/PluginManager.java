package com.github.idkp.douglas.plugin;

import com.github.idkp.douglas.plugin.registry.PluginRegistry;

import java.nio.file.Path;

public interface PluginManager {

    void load(Path pluginsDirectory);

    void loadSingle(Path pluginFile);

    void unload();

    PluginRegistry getRegistry();
}