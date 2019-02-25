package com.github.foskel.douglas.plugin.impl;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Foskel
 */
public class StandardPluginManager implements PluginManager {
    private final PluginScanningStrategy scanningStrategy;
    private final PluginRegistry registry;
    private final Collection<PluginLoadingListener> loadingListeners;

    @Inject
    StandardPluginManager(PluginScanningStrategy scanningStrategy,
                          PluginRegistry registry,
                          Collection<PluginLoadingListener> loadingListeners) {
        this.scanningStrategy = scanningStrategy;
        this.registry = registry;
        this.loadingListeners = loadingListeners;
    }

    @Override
    public void load(Path pluginsDirectory) {
        Objects.requireNonNull(pluginsDirectory, "pluginsDirectory");

        if (Files.notExists(pluginsDirectory) || !Files.isDirectory(pluginsDirectory)) {
            return;
        }

        Collection<PluginScanResult> scanResults = this.scanningStrategy.scan(pluginsDirectory);

        for (PluginScanResult scanResult : scanResults) {
            Plugin plugin = scanResult.getPlugin();

            this.registry.register(scanResult.getManifest(), plugin);
            plugin.load();
        }

        this.loadingListeners.forEach(listener -> listener.allLoaded(this.registry));
    }

    @Override
    public void loadSingle(Path pluginFile) {
        Objects.requireNonNull(pluginFile, "pluginFile");

        if (Files.notExists(pluginFile) || !Files.isRegularFile(pluginFile)) {
            return;
        }

        PluginScanResult scanResult = this.scanningStrategy.scanSingle(pluginFile);
        Plugin plugin = scanResult.getPlugin();

        this.registry.register(scanResult.getManifest(), plugin);
        plugin.load();
    }

    @Override
    public void unload() {
        for (Plugin plugin : this.registry.findAllPlugins().values()) {
            plugin.unload();
        }

        this.registry.clear();
    }

    @Override
    public PluginRegistry getRegistry() {
        return this.registry;
    }
}