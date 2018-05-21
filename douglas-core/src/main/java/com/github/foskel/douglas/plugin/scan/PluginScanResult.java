package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

public interface PluginScanResult {
    PluginDescriptor getDescriptor();

    Plugin getPlugin();
}