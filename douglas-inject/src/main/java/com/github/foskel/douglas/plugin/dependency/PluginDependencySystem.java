package com.github.foskel.douglas.plugin.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.haptor.DependencySystem;

import java.nio.file.Path;

public interface PluginDependencySystem extends DependencySystem<Path, PluginDescriptor, Plugin> {
    void satisfy(PluginLocatorService dependencyLocator);

    <T extends Plugin> T find(String groupId, String artifactId, String name, String version);
}