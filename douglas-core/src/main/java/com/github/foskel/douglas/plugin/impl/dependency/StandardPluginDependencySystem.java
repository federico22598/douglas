package com.github.foskel.douglas.plugin.impl.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.dependency.registry.PluginDependencyRegistry;
import com.github.foskel.douglas.plugin.dependency.satisfy.PluginDependencySatisfyingStrategy;
import com.github.foskel.douglas.plugin.impl.locate.SimplePluginLocator;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.DependencySatisfyingStrategy;
import com.github.foskel.haptor.validate.NullCheckingDependencyValidator;

import java.util.*;

/**
 * @author Foskel
 */
public final class StandardPluginDependencySystem implements PluginDependencySystem {
    private final PluginDependencyRegistry registry;
    private final Set<DependencyProcessor> satisfyingProcessors;
    private final DependencySatisfyingStrategy<PluginDescriptor, Plugin> dependencySatisfyingStrategy;
    private final PluginLocatorService locator;

    public StandardPluginDependencySystem() {
        this.registry = new PluginDependencyRegistry();
        this.satisfyingProcessors = new HashSet<>();
        this.dependencySatisfyingStrategy = new PluginDependencySatisfyingStrategy(NullCheckingDependencyValidator.INSTANCE);
        this.locator = new SimplePluginLocator(this.registry.findAllDependencies());
    }

    @Override
    public boolean register(PluginDescriptor source) {
        return this.registry.registerDirectly(source, null);
    }

    @Override
    public boolean unregister(PluginDescriptor source) {
        return this.registry.unregisterDirectly(source);
    }

    @Override
    public boolean registerProcessor(DependencyProcessor processor) {
        return this.satisfyingProcessors.add(processor);
    }

    @Override
    public boolean unregisterProcessor(DependencyProcessor processor) {
        return this.satisfyingProcessors.remove(processor);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends Plugin> T find(PluginDescriptor identifier) {
        return (T) this.locator.find(identifier);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends Plugin> T find(String groupId, String artifactId) {
        return (T) this.locator.find(groupId, artifactId);
    }

    @Override
    public List<DependencySatisfyingResult<PluginDescriptor, Plugin>> satisfy(Map<PluginDescriptor, Plugin> unsatisfiedDependencies) {
        return this.dependencySatisfyingStrategy.satisfy(this.registry,
                this.satisfyingProcessors,
                unsatisfiedDependencies);
    }

    @Override
    public void satisfy() {
        Map<PluginDescriptor, Plugin> dependencies = new HashMap<>();
        Set<PluginDescriptor> unsatisfiedDependencies = this.registry.findAllDependencies().keySet();

        unsatisfiedDependencies.forEach(manifest -> {
            Plugin dependency = this.locator.find(manifest);

            if (dependency != null) {
                dependencies.put(manifest, dependency);
            }
        });

        this.satisfy(dependencies);
    }

    @Override
    public DependencyRegistry<PluginDescriptor, Plugin> getRegistry() {
        return this.registry;
    }
}