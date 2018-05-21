package com.github.foskel.douglas.plugin.descriptor.extract;

import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.io.IOException;
import java.nio.file.Path;

public interface PluginDescriptorExtractor {
    PluginDescriptor extract(Path pluginContainingFile) throws IOException;
}