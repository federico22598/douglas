package com.github.idkp.douglas.dagger;

import com.github.idkp.douglas.module.ModuleManager;
import com.github.idkp.douglas.module.SynchronizedModuleManager;
import com.github.idkp.douglas.module.locate.ModuleLocatorProvider;
import com.github.idkp.douglas.module.locate.SynchronizedModuleLocatorProvider;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

@Module
public final class DouglasModulesModule {

    @Provides
    @Singleton
    static ModuleManager provideModuleManager(SynchronizedModuleManager moduleManager) {
        return moduleManager;
    }

    @Provides
    static Map<String, com.github.idkp.douglas.module.Module> provideModules() {
        return Collections.emptyMap();
    }

    @Provides
    static ModuleLocatorProvider provideModuleLocatorProvider() {
        return new SynchronizedModuleLocatorProvider();
    }
}