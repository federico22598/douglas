package com.github.foskel.douglas.plugin.load;

import com.github.foskel.douglas.plugin.registry.PluginRegistry;

/**
 * @author Foskel
 */
@FunctionalInterface
public interface PluginLoadingListener {
    void allLoaded(PluginRegistry registry);
}