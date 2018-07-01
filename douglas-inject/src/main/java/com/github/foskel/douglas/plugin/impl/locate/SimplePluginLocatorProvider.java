package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.util.Map;

/**
 * @author Foskel
 */
public final class SimplePluginLocatorProvider implements PluginLocatorProvider {

    @Override
    public PluginLocatorService createPluginLocator(Map<PluginManifest, Plugin> plugins) {
        return new SimplePluginLocator(plugins);
    }
}
