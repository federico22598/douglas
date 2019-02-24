package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.registry.PluginRegistry;

/**
 * @author Foskel
 */
public interface PluginLocatorProvider {
    PluginLocatorService createPluginLocator(PluginRegistry registry);
}