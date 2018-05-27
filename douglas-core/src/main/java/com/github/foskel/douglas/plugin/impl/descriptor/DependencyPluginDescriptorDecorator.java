package com.github.foskel.douglas.plugin.impl.descriptor;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;

import java.util.Collection;

public final class DependencyPluginDescriptorDecorator extends AbstractPluginDescriptorDecorator {
    public DependencyPluginDescriptorDecorator(PluginDescriptor backingDecoratedInformationEntry) {
        super(backingDecoratedInformationEntry);
    }

    static DependencyPluginDescriptorDecorator of(String groupId,
                                                  String artifactId,
                                                  Version version,
                                                  String name,
                                                  Collection<String> resourceTargets,
                                                  Collection<PluginDescriptor> dependencyDescriptors) {
        return new DependencyPluginDescriptorDecorator(
                new SimplePluginDescriptor(null,
                        groupId,
                        artifactId,
                        version,
                        name,
                        resourceTargets,
                        dependencyDescriptors)
        );
    }

    @Override
    public String getMainClass() {
        throw new IllegalStateException("Dependency data entries should only contain " +
                "descriptor related data");
    }
}