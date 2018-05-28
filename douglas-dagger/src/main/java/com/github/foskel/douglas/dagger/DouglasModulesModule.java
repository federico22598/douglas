package com.github.foskel.douglas.dagger;

import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.SynchronizedModuleManager;
import com.github.foskel.douglas.module.dependency.ModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.dependency.SimpleModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.SynchronizedModuleLocatorProvider;
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
    static Map<String, com.github.foskel.douglas.module.Module> provideModules() {
        return Collections.emptyMap();
    }

    @Provides
    static ModuleLocatorProvider provideModuleLocatorProvider() {
        return new SynchronizedModuleLocatorProvider();
    }

    @Provides
    static ModuleDependencySatisfyingService provideModuleDependencySatisfyingService() {
        return new SimpleModuleDependencySatisfyingService();
    }
}