package com.github.foskel.douglas.module.locate;

import com.github.foskel.douglas.module.Module;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Foskel
 */
public class SynchronizedModuleLocator implements ModuleLocatorService {
    private final Supplier<Map<String, Module>> modulesSupplier;

    public SynchronizedModuleLocator(Supplier<Map<String, Module>> modulesSupplier) {
        this.modulesSupplier = modulesSupplier;
    }

    @Override
    public Optional<Module> findModule(String identifier) {
        Objects.requireNonNull(identifier, "identifier");

        synchronized (this.modulesSupplier) {
            return Optional.ofNullable(this.modules().get(identifier));
        }
    }

    @Override
    public Set<Module> findModules(Predicate<Module> condition) {
        Objects.requireNonNull(condition, "condition");

        synchronized (this.modulesSupplier) {
            return this.modules().values()
                    .stream()
                    .filter(condition)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Optional<Module> findModule(Class moduleClass) {
        Objects.requireNonNull(moduleClass, "moduleClass");

        synchronized (this.modulesSupplier) {
            return this.modules().values()
                    .stream()
                    .filter(module -> module.getClass() == moduleClass)
                    .findFirst();
        }
    }

    private Map<String, Module> modules() {
        return this.modulesSupplier.get();
    }
}