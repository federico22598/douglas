package com.github.foskel.douglas.plugin.load;

import com.github.foskel.douglas.plugin.Plugin;

import java.util.Collection;

public interface PluginLoader {
    void load(Collection<Plugin> plugins);

    void unload(Collection<Plugin> plugins);
}