package com.github.idkp.douglas.plugin.impl.locate;

import com.github.idkp.douglas.plugin.locate.PluginLocatorProvider;
import com.github.idkp.douglas.plugin.locate.PluginLocatorService;
import com.github.idkp.douglas.plugin.registry.PluginRegistry;

public final class SimplePluginLocatorProvider implements PluginLocatorProvider {

    @Override
    public PluginLocatorService createPluginLocator(PluginRegistry registry) {
        return new SimplePluginLocator(registry);
    }
}
