package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.locate.PluginLocatorServiceProvider;

import java.util.Map;

public enum DependencyPluginLocatorServiceProvider implements PluginLocatorServiceProvider {
    INSTANCE;

    @Override
    public PluginLocatorService create(Map<PluginDescriptor, Plugin> plugins) {
        return new DependencyPluginLocatorServiceDecorator(
                new StandardPluginLocatorService(plugins)
        );
    }
}