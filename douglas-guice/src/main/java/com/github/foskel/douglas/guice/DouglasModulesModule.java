package com.github.foskel.douglas.guice;

import com.github.foskel.douglas.module.Module;
import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.SynchronizedModuleManager;
import com.github.foskel.douglas.module.dependency.ModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.dependency.SimpleModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.SynchronizedModuleLocatorProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.Collections;
import java.util.Map;

/**
 * Provides Guice bindings for the module system
 *
 * @author Foskel
 */
public final class DouglasModulesModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ModuleManager.class).to(SynchronizedModuleManager.class);
        this.bind(ModuleLocatorProvider.class).to(SynchronizedModuleLocatorProvider.class);
        this.bind(ModuleDependencySatisfyingService.class).to(SimpleModuleDependencySatisfyingService.class);
    }

    @Provides
    static Map<String, Module> provideModules() {
        return Collections.emptyMap();
    }
}