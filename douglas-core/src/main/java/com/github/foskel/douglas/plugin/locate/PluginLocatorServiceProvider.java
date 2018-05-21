package com.github.foskel.douglas.plugin.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.util.Map;

public interface PluginLocatorServiceProvider {
    PluginLocatorService create(Map<PluginDescriptor, Plugin> plugins);
}