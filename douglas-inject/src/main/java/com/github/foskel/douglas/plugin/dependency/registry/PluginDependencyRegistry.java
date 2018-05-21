package com.github.foskel.douglas.plugin.dependency.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.haptor.registry.DependencyRegistry;
import com.github.foskel.haptor.scan.DependencyScanResult;
import com.github.foskel.haptor.scan.DependencyScanningStrategy;

import java.util.*;
import java.util.stream.Collectors;

public final class PluginDependencyRegistry<S> implements DependencyRegistry<S, PluginDescriptor, Plugin> {
    private final Map<PluginDescriptor, Plugin> dependencies = new HashMap<>();

    @Override
    public boolean register(S source, DependencyScanningStrategy<PluginDescriptor, Plugin> scanningStrategy) {
        Collection<DependencyScanResult<PluginDescriptor, Plugin>> scanResults = scanningStrategy.scan(source);

        if (scanResults.isEmpty()) {
            return false;
        }

        List<PluginDescriptor> scannedDescriptors = scanResults
                .stream()
                .map(DependencyScanResult::getDependencyIdentifier)
                .collect(Collectors.toList());
        Set<PluginDescriptor> currentDescriptors = this.dependencies.keySet();

        if (currentDescriptors.containsAll(scannedDescriptors)) {
            return false;
        }

        scanResults.forEach(this::registerFromResult);

        return true;
    }

    private void registerFromResult(DependencyScanResult<PluginDescriptor, Plugin> scanResult) {
        this.dependencies.put(scanResult.getDependencyIdentifier(), scanResult.getDependency());
    }

    @Override
    public boolean registerDirectly(PluginDescriptor PluginDescriptor, Plugin dependency) {
        this.dependencies.put(PluginDescriptor, dependency);

        return true;
    }

    @Override
    public boolean unregister(S source) {
        return false;//TODO
    }

    @Override
    public boolean unregisterDirectly(PluginDescriptor PluginDescriptor) {
        if (this.dependencies.containsKey(PluginDescriptor)) {
            this.dependencies.remove(PluginDescriptor);

            return true;
        }

        return false;
    }

    @Override
    public boolean has(PluginDescriptor identifier) {
        return this.dependencies.containsKey(identifier);
    }

    @Override
    public boolean hasDependencies() {
        return !this.dependencies.isEmpty();
    }

    @Override
    public Map<PluginDescriptor, Plugin> findAllDependencies() {
        return Collections.unmodifiableMap(this.dependencies);
    }

    @Override
    public void clearDependencies() {
        this.dependencies.clear();
    }
}