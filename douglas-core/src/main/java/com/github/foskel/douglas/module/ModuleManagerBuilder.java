package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.dependency.ModuleDependencySatisfyingService;

import java.util.Map;

/**
 * @author Foskel
 */
public final class ModuleManagerBuilder {
    private final Map<String, Module> modules;
    private ModuleDependencySatisfyingService dependencySatisfier;

    public ModuleManagerBuilder(Map<String, Module> modules) {
        this.modules = modules;
    }

    public ModuleManagerBuilder dependencySatisfier(ModuleDependencySatisfyingService dependencySatisfier) {
        this.dependencySatisfier = dependencySatisfier;

        return this;
    }

    public ModuleManager build() {
        if (this.dependencySatisfier == null) {
            throw new IllegalStateException("You must finish building the ModuleManager before calling ModuleManagerBuilder#xml!");
        }

        return new SynchronizedModuleManager(this.modules, this.dependencySatisfier);
    }
}
