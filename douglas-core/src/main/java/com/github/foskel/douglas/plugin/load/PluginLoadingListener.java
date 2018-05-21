package com.github.foskel.douglas.plugin.load;

import com.github.foskel.douglas.plugin.registry.PluginRegistry;

@FunctionalInterface
public interface PluginLoadingListener {
    void allLoaded(PluginRegistry registry);
}