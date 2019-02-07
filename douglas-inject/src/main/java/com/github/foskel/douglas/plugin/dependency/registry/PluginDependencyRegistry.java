package com.github.foskel.douglas.plugin.dependency.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.scan.UnsatisfiedDependencyScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Foskel
 */
public final class PluginDependencyRegistry implements DependencyRegistry<PluginDescriptor, Plugin> {
    private final Map<PluginDescriptor, Plugin> dependencies = new HashMap<>();

    @Override
    public boolean register(Object source, UnsatisfiedDependencyScanner<PluginDescriptor> scanningStrategy) {
        Collection<PluginDescriptor> scanResults = scanningStrategy.scan(source);

        if (scanResults.isEmpty()) {
            return false;
        }

        scanResults.forEach(this::registerUnsatisfied);

        return true;
    }

    private void registerUnsatisfied(PluginDescriptor descriptor) {
        if (!this.dependencies.containsKey(descriptor)) {
            this.dependencies.put(descriptor, null);
        }
    }

    @Override
    public boolean registerDirectly(PluginDescriptor descriptor, Plugin dependency) {
        this.dependencies.put(descriptor, dependency);

        return true;
    }

    @Override
    public boolean unregister(Object source) {
        return false;//TODO
    }

    @Override
    public boolean unregisterDirectly(PluginDescriptor descriptor) {
        if (this.dependencies.containsKey(descriptor)) {
            this.dependencies.remove(descriptor);

            return true;
        }

        return false;
    }

    @Override
    public boolean unregisterIf(Predicate<PluginDescriptor> condition) {
        return this.dependencies.keySet().removeIf(condition);
    }

    @Override
    public boolean has(PluginDescriptor identifier) {
        return this.dependencies.containsKey(identifier);
    }

    @Override
    public Map<PluginDescriptor, Plugin> findAllDependencies() {
        return Collections.unmodifiableMap(this.dependencies);
    }

    @Override
    public void clear() {
        this.dependencies.clear();
    }
}