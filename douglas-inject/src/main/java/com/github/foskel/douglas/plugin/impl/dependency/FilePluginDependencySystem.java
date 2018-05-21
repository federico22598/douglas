package com.github.foskel.douglas.plugin.impl.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.dependency.registry.PluginDependencyRegistry;
import com.github.foskel.douglas.plugin.dependency.satisfy.PluginDependencySatisfyingStrategy;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.haptor.impl.registry.decorate.ImmutableDependencyRegistryDecorator;
import com.github.foskel.haptor.impl.validate.NullCheckingDependencyValidatorService;
import com.github.foskel.haptor.process.DependencySatisfyingProcessor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.DependencySatisfyingStrategy;
import com.github.foskel.haptor.scan.DependencyScanningStrategy;

import java.nio.file.Path;
import java.util.*;

public final class FilePluginDependencySystem implements PluginDependencySystem {
    private static final String LATEST_VERSION = "<latest>";

    private final PluginDependencyRegistry<Path> registry;
    private final Set<DependencySatisfyingProcessor> satisfyingProcessors;
    private final DependencyScanningStrategy<PluginDescriptor, Plugin> scanningStrategy;
    private final DependencySatisfyingStrategy dependencySatisfyingStrategy;

    public FilePluginDependencySystem(DependencyScanningStrategy<PluginDescriptor, Plugin> scanningStrategy) {
        this.scanningStrategy = scanningStrategy;
        this.registry = new PluginDependencyRegistry<>();
        this.satisfyingProcessors = new HashSet<>();
        this.dependencySatisfyingStrategy = new PluginDependencySatisfyingStrategy(NullCheckingDependencyValidatorService.INSTANCE);
    }

    @Override
    public boolean registerDependencies(Path source) {
        return this.registry.register(source, this.scanningStrategy);
    }

    @Override
    public boolean unregisterDependencies(Path source) {
        return this.registry.unregister(source);
    }

    @Override
    public boolean registerProcessor(DependencySatisfyingProcessor processor) {
        return this.satisfyingProcessors.add(processor);
    }

    @Override
    public boolean unregisterProcessor(DependencySatisfyingProcessor processor) {
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
                    //use the plugin with the latest version
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
    public Map<PluginDescriptor, Plugin> findAllDependencies() {
        return Collections.unmodifiableMap(this.registry.findAllDependencies());
    }

    @Override
    public void clearDependencies() {
        this.registry.clearDependencies();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DependencySatisfyingResult> satisfy(Map<PluginDescriptor, Plugin> unsatisfiedDependencies) {
        return this.dependencySatisfyingStrategy.satisfy(this.registry,
                this.satisfyingProcessors,
                (Map<Object, Object>) (Map<?, ?>) unsatisfiedDependencies);
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
    public DependencyRegistry<Path, PluginDescriptor, Plugin> getDependencyRegistry() {
        return new ImmutableDependencyRegistryDecorator<>(this.registry);
    }
}