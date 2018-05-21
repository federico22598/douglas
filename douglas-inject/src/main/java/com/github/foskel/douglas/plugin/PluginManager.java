package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.plugin.registry.PluginRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface PluginManager {
    void load(Path pluginsDirectory) throws IOException;

    void unload() throws IOException;

    Optional<Plugin> find(String name);

    PluginRegistry getRegistry();
}