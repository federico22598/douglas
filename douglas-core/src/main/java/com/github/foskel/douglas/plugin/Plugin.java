package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.core.traits.Loadable;
import com.github.foskel.douglas.core.traits.Reloadable;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;

public interface Plugin extends Loadable, Reloadable {

    @Override
    void load();

    @Override
    void unload();

    @Override
    default void reload() {
        this.unload();
        this.load();
    }

    PluginDependencySystem getDependencySystem();
}