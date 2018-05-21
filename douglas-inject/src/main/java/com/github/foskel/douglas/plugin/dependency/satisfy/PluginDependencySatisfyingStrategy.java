package com.github.foskel.douglas.plugin.dependency.satisfy;

import com.github.foskel.douglas.core.descriptor.Descriptor;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.haptor.exceptions.UnsatisfiedDependencyException;
import com.github.foskel.haptor.impl.satisfy.SimpleDependencySatisfyingResult;
import com.github.foskel.haptor.process.DependencySatisfyingProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.DependencySatisfyingStrategy;
import com.github.foskel.haptor.validate.DependencyValidatorService;

import java.util.*;

public final class PluginDependencySatisfyingStrategy implements DependencySatisfyingStrategy {
    private final DependencyValidatorService validatorService;

    public PluginDependencySatisfyingStrategy(DependencyValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DependencySatisfyingResult satisfy(Object dependencyIdentifier,
                                              Object dependency,
                                              DependencyRegistry registry,
                                              Set<DependencySatisfyingProcessor> processors) {
        if (dependency != null) {
            if (registry.has(dependencyIdentifier)) {
                registry.registerDirectly(dependencyIdentifier, dependency);
            }

            boolean validatingResult = this.validatorService.validate(dependency);

            DependencySatisfyingResult result = new SimpleDependencySatisfyingResult(dependencyIdentifier,
                    dependency,
                    validatingResult);

            processors.forEach(processor -> {
                try {
                    processor.postSatisfy(result);
                } catch (UnsatisfiedDependencyException exception) {
                    exception.printStackTrace();
                }
            });

            return result;
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DependencySatisfyingResult> satisfy(DependencyRegistry registry,
                                                    Set<DependencySatisfyingProcessor> processors,
                                                    Map<Object, Object> dependencies) {
        if (!dependencies.isEmpty()) {
            dependencies.forEach((identifier, dependency) -> {
                if (dependency != null) {
                    if (registry.has(identifier)) {
                        registry.registerDirectly(identifier, dependency);
                    }
                }
            });
        }

        List<DependencySatisfyingResult> results = new ArrayList<>(registry.findAllDependencies().size());

        for (Map.Entry<Descriptor, Plugin> dependencyEntry : (Set<Map.Entry>) registry.findAllDependencies().entrySet()) {
            Descriptor dependencyIdentifier = dependencyEntry.getKey();
            Plugin dependency = dependencyEntry.getValue();

            boolean validatingResult = this.validatorService.validate(dependency);

            DependencySatisfyingResult result = new SimpleDependencySatisfyingResult(dependencyIdentifier,
                    dependency,
                    validatingResult);

            processors.forEach(processor -> {
                try {
                    processor.postSatisfy(result);
                } catch (UnsatisfiedDependencyException exception) {
                    exception.printStackTrace();
                }
            });

            results.add(result);
        }

        return results;
    }
}
