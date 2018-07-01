package com.github.foskel.douglas.plugin.impl.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.dependency.registry.PluginDependencyRegistry;
import com.github.foskel.douglas.plugin.dependency.satisfy.PluginDependencySatisfyingStrategy;
import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
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
    private final DependencySatisfyingStrategy<PluginManifest, Plugin> dependencySatisfyingStrategy;
    private final PluginLocatorService locator;

    public StandardPluginDependencySystem(PluginLocatorProvider locatorProvider) {
        this.registry = new PluginDependencyRegistry();
        this.satisfyingProcessors = new HashSet<>();
        this.dependencySatisfyingStrategy = new PluginDependencySatisfyingStrategy(NullCheckingDependencyValidator.INSTANCE);
        this.locator = locatorProvider.createPluginLocator(this.registry.findAllDependencies());
    }

    @Override
    public boolean register(PluginManifest source) {
        return this.registry.registerDirectly(source, null);
    }

    @Override
    public boolean unregister(PluginManifest source) {
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
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <T extends Plugin> T find(PluginManifest identifier) {
        return (T) this.locator.find(identifier).get();
    }

    @Override
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <T extends Plugin> T find(String groupId, String artifactId, String version, String name) {
        return (T) this.locator.find(groupId, artifactId, version, name).get();
    }

    @Override
    public List<DependencySatisfyingResult<PluginManifest, Plugin>> satisfy(Map<PluginManifest, Plugin> unsatisfiedDependencies) {
        return this.dependencySatisfyingStrategy.satisfy(this.registry,
                this.satisfyingProcessors,
                unsatisfiedDependencies);
    }

    @Override
    public void satisfy() {
        Map<PluginManifest, Plugin> dependencies = new HashMap<>();
        Set<PluginManifest> unsatisfiedDependencies = this.registry.findAllDependencies().keySet();

        unsatisfiedDependencies.forEach(manifest -> {
            Optional<Plugin> candidateDependency = this.locator.find(manifest);

            candidateDependency.ifPresent(dependency -> dependencies.put(manifest, dependency));
        });

        /*
        //https://stackoverflow.com/questions/174093/toarraynew-myclass0-or-toarraynew-myclassmylist-size
        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        PluginManifest[] unsatisfiedDependenciesArray = unsatisfiedDependencies.toArray(new PluginManifest[unsatisfiedDependencies.size()]);

        for (int i = 0; i < unsatisfiedDependenciesArray.length; i++) {
            PluginManifest manifest = unsatisfiedDependenciesArray[i];
            Optional<Plugin> candidateDependency = this.locator.find(manifest);

            candidateDependency.ifPresent(dependency -> dependencies.put(manifest, dependency));
        }
        */

        this.satisfy(dependencies);
    }

    @Override
    public DependencyRegistry<PluginManifest, Plugin> getRegistry() {
        return this.registry;
    }
}