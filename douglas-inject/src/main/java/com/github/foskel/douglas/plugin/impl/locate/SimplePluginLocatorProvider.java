package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;

/**
 * @author Foskel
 */
public final class SimplePluginLocatorProvider implements PluginLocatorProvider {

    @Override
    public PluginLocatorService createPluginLocator(PluginRegistry registry) {
        return new SimplePluginLocator(registry);
    }
}
