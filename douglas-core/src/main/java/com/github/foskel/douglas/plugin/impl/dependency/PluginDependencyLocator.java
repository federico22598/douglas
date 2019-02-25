package com.github.foskel.douglas.plugin.impl.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.haptor.DependencyRef;
import com.github.foskel.haptor.DependencySystem;

import java.util.Collection;
import java.util.function.Function;

public final class PluginDependencyLocator implements Function<Object[], Plugin> {
    private final DependencySystem<PluginDescriptor, Plugin> dependencySystem;

    public PluginDependencyLocator(DependencySystem<PluginDescriptor, Plugin> dependencySystem) {
        this.dependencySystem = dependencySystem;
    }

    @Override
    public Plugin apply(Object[] args) {
        if (args.length > 1) {
            Object groupId = args[0];
            Object artifactId = args[1];

            if (groupId.getClass() == String.class && artifactId.getClass() == String.class) {
                Collection<DependencyRef<PluginDescriptor, Plugin>> dependencies = dependencySystem.getRegistry().getAllDependencies().values();

                for (DependencyRef<PluginDescriptor, Plugin> dep : dependencies) {
                    PluginDescriptor descriptor = dep.getIdentifier();

                    if (descriptor.getGroupId().equals(groupId) && descriptor.getArtifactId().equals(artifactId)) {
                        return (Plugin) dep.getValue();
                    }
                }
            }
        }

        return null;
    }
}
