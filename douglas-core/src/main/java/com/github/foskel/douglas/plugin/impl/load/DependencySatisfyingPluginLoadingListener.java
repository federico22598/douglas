package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.impl.dependency.process.PluginRemovingDependencySatisfyingProcessor;
import com.github.foskel.douglas.plugin.impl.dependency.process.supply.SupplyingDependencySatisfyingProcessor;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.haptor.process.DependencyProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DependencySatisfyingPluginLoadingListener implements PluginLoadingListener {
    private final List<DependencyProcessor> satisfyingProcessors;

    public DependencySatisfyingPluginLoadingListener(Collection<DependencyProcessor> satisfyingProcessors) {
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

        this.addDefaultProcessors(descriptor,
                plugin,
                registry);

        if (!this.satisfyingProcessors.isEmpty()) {
            this.satisfyingProcessors.forEach(dependencySystem::registerProcessor);
        }

        dependencySystem.satisfy(locatorService);
    }

    private void addDefaultProcessors(PluginDescriptor descriptor,
                                      Plugin plugin,
                                      PluginRegistry registry) {
        this.satisfyingProcessors.add(new PluginRemovingDependencySatisfyingProcessor(registry, descriptor));
        this.satisfyingProcessors.add(SupplyingDependencySatisfyingProcessor.of(registry.getLocator(), plugin));
    }
}
