package com.github.idkp.douglas.plugin.impl.dependency;

import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.douglas.plugin.manifest.PluginManifest;
import com.github.idkp.douglas.plugin.registry.PluginRegistry;
import com.github.idkp.haptor.DependencySystem;
import com.github.idkp.haptor.satisfy.UnsatisfiedDependencyException;

import java.util.Map;

public final class PluginDependencySatisfier {
    public void satisfy(PluginRegistry registry) {
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
        dependencySystem.satisfy(id -> registry.getLocator().find(id));
    }
}
