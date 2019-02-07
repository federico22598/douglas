package com.github.foskel.douglas.plugin.dependency.satisfy;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.DependencySatisfyingStrategy;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;
import com.github.foskel.haptor.validate.DependencyValidatorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Foskel
 */
public final class PluginDependencySatisfyingStrategy implements DependencySatisfyingStrategy<PluginDescriptor, Plugin> {
    private final DependencyValidatorService validatorService;

    public PluginDependencySatisfyingStrategy(DependencyValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public List<DependencySatisfyingResult<PluginDescriptor, Plugin>> satisfy(DependencyRegistry<PluginDescriptor, Plugin> registry,
                                                                              Set<DependencyProcessor> processors,
                                                                              Map<PluginDescriptor, Plugin> dependencies) {
        if (!dependencies.isEmpty()) {
            dependencies.forEach((identifier, dependency) -> {
                if (dependency != null) {
                    if (registry.has(identifier)) {
                        registry.registerDirectly(identifier, dependency);
                    }
                }
            });
        }

        List<DependencySatisfyingResult<PluginDescriptor, Plugin>> results = new ArrayList<>(registry.findAllDependencies().size());

        for (Map.Entry<PluginDescriptor, Plugin> dependencyEntry : registry.findAllDependencies().entrySet()) {
            PluginDescriptor dependencyIdentifier = dependencyEntry.getKey();
            Plugin dependency = dependencyEntry.getValue();

            boolean validatingResult = this.validatorService.validate(dependency);
            DependencySatisfyingResult<PluginDescriptor, Plugin> result = new DependencySatisfyingResult<>(dependencyIdentifier,
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
