package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.Douglas;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.impl.dependency.StandardPluginDependencySystem;

public abstract class AbstractPlugin implements Plugin {
    private final PluginDependencySystem dependencySystem = new StandardPluginDependencySystem();

    @Override
    public PluginDependencySystem getDependencySystem() {
        return this.dependencySystem;
    }
}