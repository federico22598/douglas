package com.github.foskel.douglas.module;

import com.github.foskel.haptor.DependencyRef;
import com.github.foskel.haptor.DependencySystem;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;

import java.util.Map;

/**
 * @author Foskel
 */
public final class ModuleDependencySatisfier {
    private final ModuleManager moduleManager;

    public ModuleDependencySatisfier(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void satisfy(Module module) throws UnsatisfiedDependencyException {
        DependencySystem<Class<? extends Module>, Module> dependencySystem = module.getDependencySystem();
        DependencyRegistry<Class<? extends Module>, Module> dependencyRegistry = dependencySystem.getRegistry();
        Map<Class<? extends Module>, DependencyRef<Class<? extends Module>, Module>> dependencies = dependencyRegistry.getAllDependencies();

        if (!dependencies.isEmpty()) {
            dependencySystem.registerProcessor(result -> {
                if (!result.getValidationResult()) {
                    moduleManager.unregister(module.getName());
                }
            });

            //noinspection OptionalGetWithoutIsPresent
            dependencySystem.satisfy((type) -> moduleManager.getLocator().findModule(type).get());
        }
    }
}
