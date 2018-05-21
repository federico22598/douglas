package com.github.foskel.douglas.plugin.dependency.extract;

import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface DependencyDescriptorExtractor {
    Collection<PluginDescriptor> extract(Path pluginContainingFile) throws IOException;
}