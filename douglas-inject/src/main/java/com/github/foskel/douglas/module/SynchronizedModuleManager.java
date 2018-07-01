package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.dependency.ModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.ModuleLocatorService;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.*;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * @author Foskel
 * @since 4/9/2017
 */
public final class SynchronizedModuleManager implements ModuleManager {
    private final Map<String, Module> modules;
    private final ModuleDependencySatisfyingService dependencySatisfier;
    private final ModuleLocatorService locator;

    @Inject
    SynchronizedModuleManager(Map<String, Module> modules,
                              ModuleDependencySatisfyingService dependencySatisfier,
                              ModuleLocatorProvider locatorProvider) {
        this.modules = new TreeMap<>(CASE_INSENSITIVE_ORDER);
        this.modules.putAll(modules);

        this.dependencySatisfier = dependencySatisfier;
        this.locator = locatorProvider.create(this.modules);
    }

    @Override
    public void load() {
        synchronized (this.modules) {
            this.modules.values().forEach(Module::load);
        }

        this.dependencySatisfier.satisfy(this);
    }

    @Override
    public void unload() {
        synchronized (this.modules) {
            this.modules.values().forEach(Module::unload);
            this.modules.clear();
        }
    }

    @Override
    public boolean register(Module module) {
        Objects.requireNonNull(module, "module");

        synchronized (this.modules) {
            String moduleIdentifier = module.getName();

            if (this.modules.containsKey(moduleIdentifier)) {
                return false;
            }

            this.modules.put(module.getName(), module);

            return true;
        }
    }

    @Override
    public boolean unregister(String moduleName) {
        Objects.requireNonNull(moduleName, "moduleIdentifier");

        synchronized (this.modules) {
            if (!this.modules.containsKey(moduleName)) {
                return false;
            }

            this.modules.remove(moduleName);

            return true;
        }
    }

    @Override
    public ModuleLocatorService getLocator() {
        return this.locator;
    }

    @Override
    public Collection<Module> findAllModules() {
        return Collections.unmodifiableCollection(this.modules.values());
    }

    @Override
    public void clear() {
        this.modules.clear();
    }
}