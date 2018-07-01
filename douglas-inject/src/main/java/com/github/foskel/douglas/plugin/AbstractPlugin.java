package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.Douglas;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;

/**
 * @author Foskel
 */
public abstract class AbstractPlugin implements Plugin {
    private final PluginDependencySystem dependencySystem = Douglas.newPluginDependencySystem();

    @Override
    public PluginDependencySystem getDependencySystem() {
        return this.dependencySystem;
    }
}