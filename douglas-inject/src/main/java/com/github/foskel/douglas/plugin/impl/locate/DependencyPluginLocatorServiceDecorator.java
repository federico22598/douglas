package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public final class DependencyPluginLocatorServiceDecorator implements PluginLocatorService {
    private final PluginLocatorService backingDecoratedLocatorService;

    DependencyPluginLocatorServiceDecorator(PluginLocatorService backingDecoratedLocatorService) {
        this.backingDecoratedLocatorService = backingDecoratedLocatorService;
    }

    @Override
    public Optional<Plugin> find(PluginDescriptor descriptor) {
        String groupId = descriptor.getGroupId();
        String artifactId = descriptor.getArtifactId();
        Version version = descriptor.getVersion();
        String name = descriptor.getName();

        Optional<Plugin> candidatePluginResult;

        if (version != null) {
            if (name.isEmpty()) {
                candidatePluginResult = this.find(information -> information.getGroupId().equals(groupId)
                        && information.getArtifactId().equals(artifactId)
                        && information.getVersion().equals(version));
            } else {
                candidatePluginResult = this.find(information -> information.getGroupId().equals(groupId)
                        && information.getArtifactId().equals(artifactId)
                        && information.getVersion().equals(version)
                        && information.getName().equals(name));
            }
        } else {
            if (name.isEmpty()) {
                candidatePluginResult = this.find(information -> information.getGroupId().equals(groupId)
                        && information.getArtifactId().equals(artifactId));
            } else {
                candidatePluginResult = this.find(information -> information.getGroupId().equals(groupId)
                        && information.getArtifactId().equals(artifactId)
                        && information.getName().equals(name));
            }
        }

        return candidatePluginResult;
        // return this.find(information -> information.getGroupId().equals(groupId)
        //         && information.getArtifactId().equals(artifactId)
        //         && information.getVersion().equals(version)
        //         && information.getName().equals(name));
    }

    @Override
    public Optional<Plugin> find(Predicate<PluginDescriptor> condition) {
        return this.backingDecoratedLocatorService.find(condition);
    }

    @Override
    public Set<Plugin> findAll(Predicate<Plugin> condition) {
        return this.backingDecoratedLocatorService.findAll(condition);
    }

    @Override
    public Map<PluginDescriptor, Plugin> findAllPlugins() {
        return this.backingDecoratedLocatorService.findAllPlugins();
    }
}