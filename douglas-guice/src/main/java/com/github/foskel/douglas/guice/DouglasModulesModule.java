package com.github.foskel.douglas.guice;

import com.github.foskel.douglas.module.Module;
import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.SynchronizedModuleManager;
import com.github.foskel.douglas.module.ModuleDependencySatisfier;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.SynchronizedModuleLocatorProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.*;

/**
 * Provides Guice bindings for the module system
 *
 * @author Foskel
 */
public final class DouglasModulesModule extends AbstractModule {
    private final List<Class<? extends Module>> initialModuleTypes;

    public DouglasModulesModule(Class<? extends Module>... initialModuleTypes) {
        this.initialModuleTypes = Arrays.asList(initialModuleTypes);
    }

    public DouglasModulesModule() {
        this.initialModuleTypes = Collections.emptyList();
    }

    @Provides
    @Singleton
    Map<String, Module> provideModules(Injector injector) {
        if (this.initialModuleTypes.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Module> result = new HashMap<>();

        for (Class<? extends Module> moduleType : this.initialModuleTypes) {
            Module module = injector.getInstance(moduleType);

            result.put(module.getName(), module);
        }

        return result;
    }

    @Override
    protected void configure() {
        this.bind(ModuleManager.class).to(SynchronizedModuleManager.class).in(Singleton.class);
        this.bind(ModuleLocatorProvider.class).to(SynchronizedModuleLocatorProvider.class);
    }
}