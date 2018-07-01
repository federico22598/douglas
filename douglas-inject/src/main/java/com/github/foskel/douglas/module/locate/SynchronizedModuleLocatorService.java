package com.github.foskel.douglas.module.locate;

import com.github.foskel.douglas.module.Module;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Foskel
 */
public final class SynchronizedModuleLocatorService implements ModuleLocatorService {
    private final Map<String, Module> modules;

    SynchronizedModuleLocatorService(Map<String, Module> modules) {
        Objects.requireNonNull(modules, "modules");

        this.modules = modules;
    }

    @Override
    public Optional<Module> findModule(String identifier) {
        Objects.requireNonNull(identifier, "identifier");

        synchronized (this.modules) {
            return Optional.ofNullable(this.modules.get(identifier));
        }
    }

    @Override
    public Set<Module> findModules(Predicate<Module> condition) {
        Objects.requireNonNull(condition, "condition");

        synchronized (this.modules) {
            return this.modules.values()
                    .stream()
                    .filter(condition)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Optional<Module> findModule(Class moduleClass) {
        Objects.requireNonNull(moduleClass, "moduleClass");

        synchronized (this.modules) {
            return this.modules.values()
                    .stream()
                    .filter(module -> module.getClass() == moduleClass)
                    .findFirst();
        }
    }
}