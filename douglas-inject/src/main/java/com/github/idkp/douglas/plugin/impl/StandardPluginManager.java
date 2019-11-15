package com.github.idkp.douglas.plugin.impl;

import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.PluginManager;
import com.github.idkp.douglas.plugin.impl.dependency.PluginDependencySatisfier;
import com.github.idkp.douglas.plugin.load.PluginLoader;
import com.github.idkp.douglas.plugin.load.PluginPriorityResolver;
import com.github.idkp.douglas.plugin.registry.PluginRegistry;
import com.github.idkp.douglas.plugin.scan.PluginScanResult;
import com.github.idkp.douglas.plugin.scan.PluginScanningStrategy;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public class StandardPluginManager implements PluginManager {
    private final PluginScanningStrategy scanningStrategy;
    private final PluginRegistry registry;
    private final PluginDependencySatisfier dependencySatisfier;
    private final PluginLoader pluginLoader;

    @Inject
    StandardPluginManager(PluginScanningStrategy scanningStrategy,
                          PluginRegistry registry,
                          PluginPriorityResolver priorityResolver) {
        this.scanningStrategy = scanningStrategy;
        this.registry = registry;
        this.dependencySatisfier = new PluginDependencySatisfier();
        this.pluginLoader = new PluginLoader(priorityResolver);
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
        }

        this.dependencySatisfier.satisfy(this.registry);
        this.pluginLoader.load(this.registry.findAllPlugins().values());
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
    }

    @Override
    public void unload() {
        this.pluginLoader.unload(this.registry.findAllPlugins().values());
        this.registry.clear();
    }

    @Override
    public PluginRegistry getRegistry() {
        return this.registry;
    }
}