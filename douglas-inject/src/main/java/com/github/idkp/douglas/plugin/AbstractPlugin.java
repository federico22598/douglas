package com.github.idkp.douglas.plugin;

import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.haptor.DependencySystem;
import com.github.idkp.haptor.Haptor;

public abstract class AbstractPlugin implements Plugin {
    private final DependencySystem<PluginDescriptor, Plugin> dependencySystem = Haptor.newDependencySystem();

    @Override
    public DependencySystem<PluginDescriptor, Plugin> getDependencySystem() {
        return this.dependencySystem;
    }
}