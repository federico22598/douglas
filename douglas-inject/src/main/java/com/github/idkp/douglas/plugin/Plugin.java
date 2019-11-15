package com.github.idkp.douglas.plugin;

import com.github.idkp.douglas.core.traits.Loadable;
import com.github.idkp.douglas.core.traits.Reloadable;
import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.haptor.DependencySystem;

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

    DependencySystem<PluginDescriptor, Plugin> getDependencySystem();
}