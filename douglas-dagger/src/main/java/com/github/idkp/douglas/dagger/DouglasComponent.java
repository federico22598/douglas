package com.github.idkp.douglas.dagger;

import com.github.idkp.douglas.module.ModuleManager;
import com.github.idkp.douglas.plugin.PluginManager;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DouglasPluginsModule.class, DouglasModulesModule.class})
public interface DouglasComponent {
    PluginManager pluginManager();

    ModuleManager moduleManager();

    void inject(Object obj);
}
