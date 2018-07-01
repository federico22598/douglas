package com.github.foskel.douglas.dagger;

import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Core component for API modules
 *
 * @author Foskel
 */
@Singleton
@Component(modules = {DouglasPluginsModule.class, DouglasModulesModule.class})
public interface DouglasComponent {
    PluginManager pluginManager();

    ModuleManager moduleManager();

    void inject(Object obj);
}
