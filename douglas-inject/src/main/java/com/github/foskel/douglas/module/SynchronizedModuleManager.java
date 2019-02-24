package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.ModuleLocatorService;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;
import com.github.foskel.haptor.scan.AnnotationDependencyScanner;
import com.github.foskel.haptor.scan.UnsatisfiedDependencyScanner;

import javax.inject.Inject;
import java.util.*;

import static java.lang.String.CASE_INSENSITIVE_ORDER;

/**
 * @author Foskel
 * @since 4/9/2017
 */
public class SynchronizedModuleManager implements ModuleManager {
    private static final UnsatisfiedDependencyScanner DEPENDENCY_SCANNER = new AnnotationDependencyScanner();

    private final Map<String, Module> modules;
    private final ModuleLocatorService locator;
    private final ModuleDependencySatisfier dependencySatisfier;

    @Inject
    SynchronizedModuleManager(Map<String, Module> modules, ModuleLocatorProvider locatorProvider) {
        this.dependencySatisfier = new ModuleDependencySatisfier(this);
        this.modules = new TreeMap<>(CASE_INSENSITIVE_ORDER);
        this.modules.putAll(modules);

        this.locator = locatorProvider.create(this.modules);
    }

    @Override
    public void load() {
        synchronized (this.modules) {
            for (Module module : this.modules.values()) {
                module.getDependencySystem().register(module, DEPENDENCY_SCANNER);

                try {
                    this.dependencySatisfier.satisfy(module);
                } catch (UnsatisfiedDependencyException e) {
                    e.printStackTrace();
                    continue;
                }

                module.load();
            }
        }
    }

    @Override
    public void unload() {
        synchronized (this.modules) {
            for (Module module : this.modules.values()) {
                module.unload();
                module.getDependencySystem().unregister(module);
            }

            this.modules.clear();
        }
    }

    @Override
    public boolean register(Module module) {
        Objects.requireNonNull(module, "module");

        String moduleIdentifier = module.getName();

        synchronized (this.modules) {
            if (this.modules.containsKey(moduleIdentifier)) {
                return false;
            }

            this.modules.put(module.getName(), module);

            return true;
        }
    }

    @Override
    public boolean unregister(String moduleName) {
        Objects.requireNonNull(moduleName, "moduleName");

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