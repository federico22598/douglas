package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.core.traits.Loadable;
import com.github.foskel.douglas.core.traits.Reloadable;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.haptor.DependencySystem;

/**
 * This interface serves as a model for a plugin's main class.
 * All plugins should have a main class extending this interface.
 *
 * @author Foskel
 */
public interface Plugin extends Loadable, Reloadable {

    /**
     * This acts as the "main" method for a plugin. It gets called immediately after the
     * implementation of this interface is instantiated.
     */
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