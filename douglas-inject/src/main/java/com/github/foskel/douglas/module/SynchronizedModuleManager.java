package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.dependency.ModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.io.ModulePropertyLoader;
import com.github.foskel.douglas.module.io.ModulePropertySaver;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.ModuleLocatorService;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * Synchronized implementation of {@link ModuleManager}
 *
 * @author Fred
 * @since 4/9/2017
 */
public final class SynchronizedModuleManager implements ModuleManager {
    private final Map<String, Module> modules;
    private final ModuleLocatorProvider locatorProvider;
    private final ModulePropertyLoader propertyLoader;
    private final ModulePropertySaver propertySaver;
    private final ModuleDependencySatisfyingService dependencySatisfier;
    private volatile ModuleLocatorService locator;

    @Inject
    SynchronizedModuleManager(Map<String, Module> modules,
                              ModuleLocatorProvider locatorProvider,
                              ModulePropertyLoader propertyLoader,
                              ModulePropertySaver propertySaver,
                              ModuleDependencySatisfyingService dependencySatisfier) {
        this.modules = new TreeMap<>(CASE_INSENSITIVE_ORDER);
        this.modules.putAll(modules);

        this.locatorProvider = locatorProvider;
        this.propertyLoader = propertyLoader;
        this.propertySaver = propertySaver;
        this.dependencySatisfier = dependencySatisfier;
    }

    @Override
    public void load(Path directory) throws IOException {
        Objects.requireNonNull(directory, "directory");

        synchronized (this.modules) {
            this.propertyLoader.load(this.modules.values(), directory);
        }

        this.dependencySatisfier.satisfy(this);
    }

    @Override
    public void unload(Path directory) throws IOException {
        Objects.requireNonNull(directory, "directory");

        synchronized (this.modules) {
            this.propertySaver.save(this.modules.values(), directory);

            this.modules.values().forEach(Module::unload);
            this.modules.clear();
        }
    }

    @Override
    public boolean register(Module module) {
        Objects.requireNonNull(module, "module");

        synchronized (this.modules) {
            String moduleIdentifier = module.getName();

            if (this.modules.containsKey(moduleIdentifier) || this.modules.containsValue(module)) {
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
        synchronized (this) {
            if (this.locator != null) {
                return this.locator;
            }

            this.locator = this.locatorProvider.create(this.modules);
        }

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