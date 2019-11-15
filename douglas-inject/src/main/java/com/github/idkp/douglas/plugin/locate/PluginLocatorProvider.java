package com.github.idkp.douglas.plugin.locate;

import com.github.idkp.douglas.plugin.registry.PluginRegistry;

public interface PluginLocatorProvider {
    PluginLocatorService createPluginLocator(PluginRegistry registry);
}