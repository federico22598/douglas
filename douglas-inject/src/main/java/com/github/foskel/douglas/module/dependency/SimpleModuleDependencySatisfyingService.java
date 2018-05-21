package com.github.foskel.douglas.module.dependency;

import com.github.foskel.douglas.module.Module;
import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.locate.ModuleLocatorService;
import com.github.foskel.douglas.util.Exceptions;
import com.github.foskel.haptor.exceptions.UnsatisfiedDependencyException;
import com.github.foskel.haptor.impl.ClassDependencySystem;
import com.github.foskel.haptor.impl.process.ObservableDependencySatisfyingProcessorBuilder;
import com.github.foskel.haptor.process.DependencySatisfyingProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class SimpleModuleDependencySatisfyingService implements ModuleDependencySatisfyingService {

    private static boolean hasDependencies(Module module) {
        ClassDependencySystem<Module> dependencySystem = module.getDependencySystem();
        DependencyRegistry<Object, Class<? extends Module>, Module> dependencyRegistry =
                dependencySystem.getDependencyRegistry();

        return dependencyRegistry.hasDependencies();
    }

    @Override
    public void satisfy(ModuleManager moduleManager) {
        moduleManager.findAllModules()
                .stream()
                .filter(SimpleModuleDependencySatisfyingService::hasDependencies)
                .forEach(module -> this.satisfyModuleDependencies(module, moduleManager));
    }

    private void satisfyModuleDependencies(Module module, ModuleManager moduleManager) {
        Map<Class<? extends Module>, Module> dependencies = module.getDependencySystem()
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
        ClassDependencySystem<Module> dependencySystem = module.getDependencySystem();
        DependencySatisfyingProcessor moduleRemoveProcessor = new ObservableDependencySatisfyingProcessorBuilder()
                .withFailedValidationListener(object -> moduleManager.unregister(module.getName()))

                .build();

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
