package com.github.foskel.douglas.plugin.impl.dependency.extract;

import com.github.foskel.douglas.plugin.dependency.extract.DependencyDescriptorExtractor;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public final class StandardDependencyDescriptorExtractor implements DependencyDescriptorExtractor {
    private final PluginDescriptorExtractor backingDescriptorExtractor;

    public StandardDependencyDescriptorExtractor(PluginDescriptorExtractor backingDescriptorExtractor) {
        this.backingDescriptorExtractor = backingDescriptorExtractor;
    }

    @Override
    public Collection<PluginDescriptor> extract(Path pluginContainingFile) throws IOException {
        return this.backingDescriptorExtractor
                .extract(pluginContainingFile)
                .getDependencyDescriptors();
    }
}