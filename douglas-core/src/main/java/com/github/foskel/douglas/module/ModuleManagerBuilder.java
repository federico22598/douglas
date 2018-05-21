package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.dependency.ModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.io.ModulePropertyLoader;
import com.github.foskel.douglas.module.io.ModulePropertySaver;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;

import java.util.Map;

public final class ModuleManagerBuilder {
    private final Map<String, Module> modules;
    private ModuleLocatorProvider locatorProvider;
    private ModulePropertyLoader propertyLoader;
    private ModulePropertySaver propertySaver;
    private ModuleDependencySatisfyingService dependencySatisfier;

    public ModuleManagerBuilder(Map<String, Module> modules) {
        this.modules = modules;
    }

    public ModuleManagerBuilder locatorProvider(ModuleLocatorProvider locatorProvider) {
        this.locatorProvider = locatorProvider;

        return this;
    }

    public ModuleManagerBuilder propertyLoader(ModulePropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;

        return this;
    }

    public ModuleManagerBuilder propertySaver(ModulePropertySaver propertySaver) {
        this.propertySaver = propertySaver;

        return this;
    }

    public ModuleManagerBuilder dependencySatisfier(ModuleDependencySatisfyingService dependencySatisfier) {
        this.dependencySatisfier = dependencySatisfier;

        return this;
    }

    public ModuleManager build() {
        if (this.locatorProvider == null
                || this.propertyLoader == null
                || this.propertySaver == null
                || this.dependencySatisfier == null) {
            throw new IllegalStateException("You must finish building the ModuleManager before calling ModuleManagerBuilder#build!");
        }

        return new SynchronizedModuleManager(this.modules,
                this.locatorProvider,
                this.propertyLoader,
                this.propertySaver,
                this.dependencySatisfier);
    }
}
