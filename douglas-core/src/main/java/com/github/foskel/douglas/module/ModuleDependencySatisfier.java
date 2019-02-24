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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void satisfy(Module module) throws UnsatisfiedDependencyException {
        DependencySystem<Class<? extends Module>, Module> dependencySystem = module.getDependencySystem();
        DependencyRegistry<Class<? extends Module>, Module> dependencyRegistry = dependencySystem.getRegistry();
        Map<Class<? extends Module>, DependencyRef<Class<? extends Module>, Module>> dependencies = dependencyRegistry.getAllDependencies();

        if (!dependencies.isEmpty()) {
            dependencySystem.setCustomLocator(args -> {
                Object firstArg = args[0];

                if (firstArg instanceof Class<?>) {
                    //noinspection unchecked
                    return moduleManager.getLocator().findModule((Class<? extends Module>) firstArg).get();
                }

                return null;
            });

            dependencySystem.registerProcessor(result -> {
                if (!result.getValidationResult()) {
                    moduleManager.unregister(module.getName());
                }
            });

            dependencySystem.satisfy((type) -> moduleManager.getLocator().findModule(type).get());
        }
    }
}
