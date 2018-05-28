package com.github.foskel.douglas.plugin.dependency.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.scan.UnsatisfiedDependencyScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class PluginDependencyRegistry implements DependencyRegistry<PluginManifest, Plugin> {
    private final Map<PluginManifest, Plugin> dependencies = new HashMap<>();

    @Override
    public boolean register(Object source, UnsatisfiedDependencyScanner<PluginManifest> scanningStrategy) {
        Collection<PluginManifest> scanResults = scanningStrategy.scan(source);

        if (scanResults.isEmpty()) {
            return false;
        }

        scanResults.forEach(this::registerUnsatisfied);

        return true;
    }

    private void registerUnsatisfied(PluginManifest descriptor) {
        if (!this.dependencies.containsKey(descriptor)) {
            this.dependencies.put(descriptor, null);
        }
    }

    @Override
    public boolean registerDirectly(PluginManifest descriptor, Plugin dependency) {
        this.dependencies.put(descriptor, dependency);

        return true;
    }

    @Override
    public boolean unregister(Object source) {
        return false;//TODO
    }

    @Override
    public boolean unregisterDirectly(PluginManifest descriptor) {
        if (this.dependencies.containsKey(descriptor)) {
            this.dependencies.remove(descriptor);

            return true;
        }

        return false;
    }

    @Override
    public boolean unregisterIf(Predicate<PluginManifest> condition) {
        return this.dependencies.keySet().removeIf(condition);
    }

    @Override
    public boolean has(PluginManifest identifier) {
        return this.dependencies.containsKey(identifier);
    }

    @Override
    public Map<PluginManifest, Plugin> findAllDependencies() {
        return Collections.unmodifiableMap(this.dependencies);
    }

    @Override
    public void clear() {
        this.dependencies.clear();
    }
}