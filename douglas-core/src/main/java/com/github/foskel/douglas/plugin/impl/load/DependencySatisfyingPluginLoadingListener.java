package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.dependency.PluginRemovingProcessor;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.haptor.DependencySystem;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        for (Map.Entry<PluginManifest, Plugin> entry : registry.findAllPlugins().entrySet()) {
            try {
                this.satisfyPluginDependencies(entry.getKey(), entry.getValue(), registry);
            } catch (UnsatisfiedDependencyException e) {
                e.printStackTrace();
            }
        }
    }

    private void satisfyPluginDependencies(PluginManifest manifest, Plugin plugin, PluginRegistry registry) throws UnsatisfiedDependencyException {
        DependencySystem<PluginDescriptor, Plugin> dependencySystem = plugin.getDependencySystem();

        dependencySystem.registerProcessor(new PluginRemovingProcessor(registry, manifest));
        this.satisfyingProcessors.forEach(dependencySystem::registerProcessor);
        dependencySystem.satisfy(id -> registry.getLocator().find(id));
    }
}
