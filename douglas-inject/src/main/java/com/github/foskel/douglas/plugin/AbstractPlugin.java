package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.haptor.DependencySystem;
import com.github.foskel.haptor.Haptor;

/**
 * @author Foskel
 */
public abstract class AbstractPlugin implements Plugin {
    private final DependencySystem<PluginDescriptor, Plugin> dependencySystem = Haptor.newDependencySystem();

    @Override
    public DependencySystem<PluginDescriptor, Plugin> getDependencySystem() {
        return this.dependencySystem;
    }
}