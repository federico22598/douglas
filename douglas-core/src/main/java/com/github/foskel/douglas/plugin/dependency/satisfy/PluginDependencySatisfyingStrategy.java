package com.github.foskel.douglas.plugin.dependency.satisfy;

import com.github.foskel.douglas.plugin.Plugin;
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
public final class PluginDependencySatisfyingStrategy implements DependencySatisfyingStrategy<PluginManifest, Plugin> {
    private final DependencyValidatorService validatorService;

    public PluginDependencySatisfyingStrategy(DependencyValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public List<DependencySatisfyingResult<PluginManifest, Plugin>> satisfy(DependencyRegistry<PluginManifest, Plugin> registry,
                                                                            Set<DependencyProcessor> processors,
                                                                            Map<PluginManifest, Plugin> dependencies) {
        if (!dependencies.isEmpty()) {
            dependencies.forEach((identifier, dependency) -> {
                if (dependency != null) {
                    if (registry.has(identifier)) {
                        registry.registerDirectly(identifier, dependency);
                    }
                }
            });
        }

        List<DependencySatisfyingResult<PluginManifest, Plugin>> results = new ArrayList<>(registry.findAllDependencies().size());

        for (Map.Entry<PluginManifest, Plugin> dependencyEntry : registry.findAllDependencies().entrySet()) {
            PluginManifest dependencyIdentifier = dependencyEntry.getKey();
            Plugin dependency = dependencyEntry.getValue();

            boolean validatingResult = this.validatorService.validate(dependency);
            DependencySatisfyingResult<PluginManifest, Plugin> result = new DependencySatisfyingResult<>(dependencyIdentifier,
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
