package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.impl.dependency.process.PluginRemovingProcessor;
import com.github.foskel.douglas.plugin.impl.dependency.process.supply.SupplyingDependencySatisfyingProcessor;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.haptor.process.DependencyProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Foskel
 */
public final class DependencySatisfyingPluginLoadingListener implements PluginLoadingListener {
    private final List<DependencyProcessor> satisfyingProcessors;

    public DependencySatisfyingPluginLoadingListener(Collection<DependencyProcessor> satisfyingProcessors) {
        this.satisfyingProcessors = new ArrayList<>(satisfyingProcessors);
    }

    @Override
    public void allLoaded(PluginRegistry registry) {
        registry.findAllPlugins().forEach((descriptor, plugin) -> this.satisfyPluginDependencies(descriptor, plugin, registry));
    }

    private void satisfyPluginDependencies(PluginManifest manifest, Plugin plugin, PluginRegistry registry) {
        PluginDependencySystem dependencySystem = plugin.getDependencySystem();

        dependencySystem.registerProcessor(new PluginRemovingProcessor(registry, manifest));
        dependencySystem.registerProcessor(SupplyingDependencySatisfyingProcessor.of(registry.getLocator(), plugin));

        this.satisfyingProcessors.forEach(dependencySystem::registerProcessor);

        dependencySystem.satisfy(registry.getLocator());
    }
}
