package com.github.foskel.douglas.module.dependency;

import com.github.foskel.douglas.module.Module;
import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.locate.ModuleLocatorService;
import com.github.foskel.douglas.util.Exceptions;
import com.github.foskel.haptor.DependencySystem;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Foskel
 */
public final class SimpleModuleDependencySatisfyingService implements ModuleDependencySatisfyingService {

    private static boolean hasDependencies(Module module) {
        DependencySystem<Module, Class<? extends Module>, Module> dependencySystem = module.getDependencySystem();
        DependencyRegistry<Class<? extends Module>, Module> dependencyRegistry = dependencySystem.getRegistry();

        return !dependencyRegistry.findAllDependencies().isEmpty();
    }

    @Override
    public void satisfy(ModuleManager moduleManager) {
        moduleManager.findAllModules()
                .stream()
                .filter(SimpleModuleDependencySatisfyingService::hasDependencies)
                .forEach(module -> this.satisfyModuleDependencies(module, moduleManager));
    }

    private void satisfyModuleDependencies(Module module, ModuleManager moduleManager) {
        Map<Class<? extends Module>, Module> dependencies = module.getDependencySystem().getRegistry()
                .findAllDependencies()
                .entrySet()
                .stream()
                .map(entry -> this.getDependency(entry.getKey(), entry.getValue(), moduleManager))
                .collect(Collectors.toMap(
                        ModuleDependencyEntry::getIdentifier,
                        ModuleDependencyEntry::getDependency));

        this.registerDefaultDependencyProcessors(module, moduleManager);

        module.getDependencySystem().satisfy(dependencies);
    }

    private void registerDefaultDependencyProcessors(Module module, ModuleManager moduleManager) {
        DependencySystem<Module, Class<? extends Module>, Module> dependencySystem = module.getDependencySystem();
        DependencyProcessor moduleRemoveProcessor = result -> {
            if (!result.getValidationResult()) {
                moduleManager.unregister(module.getName());
            }
        };

        dependencySystem.registerProcessor(moduleRemoveProcessor);
    }

    private ModuleDependencyEntry getDependency(Class<? extends Module> identifier,
                                                Module dependency,
                                                ModuleManager moduleManager) {
        ModuleLocatorService moduleLocatorService = moduleManager.getLocator();
        Optional<Module> dependencyResult = moduleLocatorService.findModule(identifier);

        if (!dependencyResult.isPresent()) {
            moduleManager.unregister(dependency.getName());

            Exceptions.throwAsUnchecked(new UnsatisfiedDependencyException(identifier));
        }

        return new ModuleDependencyEntry(identifier, dependencyResult.get());
    }

    private static class ModuleDependencyEntry {
        private final Class<? extends Module> identifier;
        private final Module dependency;

        ModuleDependencyEntry(Class<? extends Module> identifier,
                              Module dependency) {
            this.identifier = identifier;
            this.dependency = dependency;
        }

        Class<? extends Module> getIdentifier() {
            return this.identifier;
        }

        Module getDependency() {
            return this.dependency;
        }
    }
}
