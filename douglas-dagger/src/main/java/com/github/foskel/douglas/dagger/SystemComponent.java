package com.github.foskel.douglas.dagger;

import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import dagger.Component;

@Component(modules = {PluginsModule.class, DouglasModulesModule.class})
public interface SystemComponent {
    PluginManager pluginManager();

    PluginRegistry pluginRegistry();

    ModuleManager moduleManager();

    void inject(Object obj);
}
