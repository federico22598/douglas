package com.github.foskel.douglas.plugin.impl;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public final class StandardPluginManager implements PluginManager {
    private final PluginScanningStrategy scanningStrategy;
    private final PluginRegistry registry;
    private final PluginLoader loader;
    private final Collection<PluginLoadingListener> loadingListeners;

    @Inject
    StandardPluginManager(PluginScanningStrategy scanningStrategy,
                          PluginRegistry registry,
                          PluginLoader loader,
                          Collection<PluginLoadingListener> loadingListeners) {
        this.scanningStrategy = scanningStrategy;
        this.registry = registry;
        this.loader = loader;
        this.loadingListeners = loadingListeners;
    }

    @Override
    public void load(Path pluginsDirectory) throws IOException {
        Objects.requireNonNull(pluginsDirectory, "pluginsDirectory");

        if (Files.notExists(pluginsDirectory)) {
            Files.createDirectories(pluginsDirectory);
        }

        Collection<PluginScanResult> scanResults = this.scanningStrategy.scan(pluginsDirectory);

        this.registerScannedPlugins(scanResults);
        this.loadAllPlugins();
    }

    private void registerScannedPlugins(Collection<PluginScanResult> scanResults) {
        //We can't load them now, since the dependencies aren't satisfied yet.
        scanResults.forEach(scanResult -> this.registry.register(
                scanResult.getDescriptor(),
                scanResult.getPlugin()));
    }

    private void loadAllPlugins() {
        Collection<Plugin> plugins = this.registry
                .findAllPlugins()
                .values();

        this.loadingListeners.forEach(processor -> processor.allLoaded(this.registry));
        this.loader.load(plugins);
    }

    @Override
    public void unload() {
        this.unloadAllPlugins();
        this.registry.clear();
    }

    private void unloadAllPlugins() {
        Collection<Plugin> plugins = this.registry
                .findAllPlugins()
                .values();

        this.loader.unload(plugins);
    }

    @Override
    public Optional<Plugin> find(String name) {
        Objects.requireNonNull(name, "name");

        return this.registry
                .getLocator()
                .find(descriptor -> descriptor.getName().equals(name));
    }

    @Override
    public PluginRegistry getRegistry() {
        return this.registry;
    }
}