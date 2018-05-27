package com.github.foskel.douglas.plugin.impl.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.dependency.registry.PluginDependencyRegistry;
import com.github.foskel.douglas.plugin.dependency.satisfy.PluginDependencySatisfyingStrategy;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.registry.decorate.ImmutableDependencyRegistryDecorator;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.DependencySatisfyingStrategy;
import com.github.foskel.haptor.validate.NullCheckingDependencyValidator;

import java.util.*;

public final class StandardPluginDependencySystem implements PluginDependencySystem {
    private static final String LATEST_VERSION = "<latest>";

    private final PluginDependencyRegistry registry;
    private final Set<DependencyProcessor> satisfyingProcessors;
    private final DependencySatisfyingStrategy<PluginDescriptor, Plugin> dependencySatisfyingStrategy;

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
    @SuppressWarnings("unchecked")
    public <T extends Plugin> T find(PluginDescriptor identifier) {
        return (T) this.registry.findAllDependencies().get(identifier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Plugin> T find(String groupId, String artifactId, String name, String version) {
        PluginDescriptor previousDescriptor = null;
        Plugin result = null;

        for (Map.Entry<PluginDescriptor, Plugin> entry : this.registry.findAllDependencies().entrySet()) {
            PluginDescriptor descriptor = entry.getKey();

            if (matches(descriptor, groupId, artifactId, name)) {
                if (version == null || version.equals(LATEST_VERSION)) {
                    // use the latest plugin

                    if (previousDescriptor != null && previousDescriptor.compareTo(descriptor) < 0) {
                        result = entry.getValue();
                    }
                } else if (version.equals(descriptor.getVersion().toString())) {
                    result = entry.getValue();
                }
            }

            previousDescriptor = descriptor;
        }

        return (T) result;
    }

    private static boolean matches(PluginDescriptor descriptor,
                                   String groupId,
                                   String artifactId,
                                   String name) {
        return descriptor.getGroupId().equals(groupId)
                && descriptor.getArtifactId().equals(artifactId)
                && descriptor.getName().equals(name);
    }

    @Override
    public List<DependencySatisfyingResult<PluginDescriptor, Plugin>> satisfy(Map<PluginDescriptor, Plugin> unsatisfiedDependencies) {
        return this.dependencySatisfyingStrategy.satisfy(this.registry,
                this.satisfyingProcessors,
                unsatisfiedDependencies);
    }

    @Override
    public void satisfy(PluginLocatorService dependencyLocator) {
        Map<PluginDescriptor, Plugin> dependencies = new HashMap<>();
        Set<PluginDescriptor> unsatisfiedDependencies = this.registry.findAllDependencies().keySet();

        //https://stackoverflow.com/questions/174093/toarraynew-myclass0-or-toarraynew-myclassmylist-size
        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        PluginDescriptor[] unsatisfiedDependenciesArray = unsatisfiedDependencies.toArray(new PluginDescriptor[unsatisfiedDependencies.size()]);

        Arrays.stream(unsatisfiedDependenciesArray, 0, unsatisfiedDependenciesArray.length).forEach(identifier -> {
            Optional<Plugin> candidateDependency = dependencyLocator.find(identifier);

            candidateDependency.ifPresent(dependency -> dependencies.put(identifier, dependency));
        });

        this.satisfy(dependencies);
    }

    @Override
    public DependencyRegistry<PluginDescriptor, Plugin> getRegistry() {
        return new ImmutableDependencyRegistryDecorator<>(this.registry);
    }
}