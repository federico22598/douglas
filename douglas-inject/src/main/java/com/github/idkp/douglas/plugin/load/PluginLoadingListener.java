package com.github.idkp.douglas.plugin.load;

import com.github.idkp.douglas.plugin.registry.PluginRegistry;

@FunctionalInterface
public interface PluginLoadingListener {
    void allLoaded(PluginRegistry registry);
}