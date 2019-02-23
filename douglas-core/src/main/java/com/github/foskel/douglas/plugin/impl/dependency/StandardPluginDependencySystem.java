package com.github.foskel.douglas.plugin.impl.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.dependency.registry.PluginDependencyRegistry;
import com.github.foskel.douglas.plugin.dependency.satisfy.PluginDependencySatisfyingStrategy;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.util.Exceptions;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.DependencySatisfyingStrategy;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;
import com.github.foskel.haptor.validate.NullCheckingDependencyValidator;

import java.util.*;

/**
 * @author Foskel
 */
public final class StandardPluginDependencySystem implements PluginDependencySystem {
    private final PluginDependencyRegistry registry;
    private final Set<DependencyProcessor> satisfyingProcessors;
    private final DependencySatisfyingStrategy<PluginDescriptor, Plugin> dependencySatisfyingStrategy;
    private PluginLocatorService dependencyLocator;

    public StandardPluginDependencySystem() {
        this.registry = new PluginDependencyRegistry();
        this.satisfyingProcessors = new HashSet<>();
        this.dependencySatisfyingStrategy = new PluginDependencySatisfyingStrategy(NullCheckingDependencyValidator.INSTANCE);
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
        if (this.dependencyLocator == null) {
            Exceptions.throwAsUnchecked(new UnsatisfiedDependencyException(identifier));
            return null;
        }

        return (T) this.dependencyLocator.find(identifier);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends Plugin> T find(String groupId, String artifactId) {
        if (this.dependencyLocator == null) {
            Exceptions.throwAsUnchecked(new UnsatisfiedDependencyException(artifactId));
            return null;
        }

        return (T) this.dependencyLocator.find(groupId, artifactId);
    }

    @Override
    public List<DependencySatisfyingResult<PluginDescriptor, Plugin>> satisfy(Map<PluginDescriptor, Plugin> unsatisfiedDependencies) {
        return this.dependencySatisfyingStrategy.satisfy(this.registry, this.satisfyingProcessors, unsatisfiedDependencies);
    }

    @Override
    public void satisfy(PluginLocatorService dependencyLocator) {
        this.dependencyLocator = dependencyLocator;

        Map<PluginDescriptor, Plugin> dependencies = new HashMap<>();
        Set<PluginDescriptor> unsatisfiedDependencies = this.registry.findAllDependencies().keySet();

        unsatisfiedDependencies.forEach(manifest -> {
            Plugin dependency = this.dependencyLocator.find(manifest);

            if (dependency != null) {
                dependencies.put(manifest, dependency);
            }
        });

        /*
        //https://stackoverflow.com/questions/174093/toarraynew-myclass0-or-toarraynew-myclassmylist-size
        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        PluginDescriptor[] unsatisfiedDependenciesArray = unsatisfiedDependencies.toArray(new PluginDescriptor[unsatisfiedDependencies.size()]);

        for (int i = 0; i < unsatisfiedDependenciesArray.length; i++) {
            PluginDescriptor manifest = unsatisfiedDependenciesArray[i];
            Optional<Plugin> candidateDependency = this.locator.find(manifest);

            candidateDependency.ifPresent(dependency -> dependencies.put(manifest, dependency));
        }
        */

        this.satisfy(dependencies);
    }

    @Override
    public DependencyRegistry<PluginDescriptor, Plugin> getRegistry() {
        return this.registry;
    }
}