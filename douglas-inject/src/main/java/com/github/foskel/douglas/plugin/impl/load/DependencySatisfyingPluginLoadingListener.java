package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.impl.dependency.process.PluginRemovingDependencySatisfyingProcessor;
import com.github.foskel.douglas.plugin.impl.dependency.process.supply.SupplyingDependencySatisfyingProcessor;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.haptor.process.DependencySatisfyingProcessor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DependencySatisfyingPluginLoadingListener implements PluginLoadingListener {
    private final List<DependencySatisfyingProcessor> satisfyingProcessors;

    public DependencySatisfyingPluginLoadingListener(Collection<DependencySatisfyingProcessor> satisfyingProcessors) {
        this.satisfyingProcessors = new ArrayList<>(satisfyingProcessors);
    }

    @Override
    public void allLoaded(PluginRegistry registry) {
        registry.findAllPlugins().forEach((descriptor, plugin) -> this.satisfyPluginDependencies(descriptor, plugin, registry));
    }

    private void satisfyPluginDependencies(PluginDescriptor descriptor,
                                           Plugin plugin,
                                           PluginRegistry registry) {
        PluginDependencySystem dependencySystem = plugin.getDependencySystem();
        PluginLocatorService locatorService = registry.getLocator();

        this.registerDefaultDependencyProcessors(descriptor,
                plugin,
                registry);

        if (!this.satisfyingProcessors.isEmpty()) {
            this.satisfyingProcessors.forEach(dependencySystem::registerProcessor);
        }

        dependencySystem.satisfy(locatorService);
    }

    private void registerDefaultDependencyProcessors(PluginDescriptor descriptor,
                                                     Plugin plugin,
                                                     PluginRegistry registry) {
        DependencySatisfyingProcessor removeProcessor = new PluginRemovingDependencySatisfyingProcessor(
                registry, descriptor);

        PluginLocatorService locatorService = registry.getLocator();
        DependencySatisfyingProcessor supplyProcessor = SupplyingDependencySatisfyingProcessor.of(
                locatorService, plugin);

        this.satisfyingProcessors.add(removeProcessor);
        this.satisfyingProcessors.add(supplyProcessor);
    }
}
