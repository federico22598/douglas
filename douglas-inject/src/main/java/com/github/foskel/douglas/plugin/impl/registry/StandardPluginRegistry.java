package com.github.foskel.douglas.plugin.impl.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class StandardPluginRegistry implements PluginRegistry {
    private final Map<PluginManifest, Plugin> plugins;
    private final PluginLocatorService locator;

    @Inject
    StandardPluginRegistry(PluginLocatorProvider locatorProvider) {
        this.plugins = new ConcurrentHashMap<>();
        this.locator = locatorProvider.createPluginLocator(this.plugins);
    }

    @Override
    public boolean register(PluginManifest manifest, Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        if (!this.plugins.containsKey(manifest)) {
            this.plugins.put(manifest, plugin);

            return true;
        }

        return false;
    }

    @Override
    public boolean registerAll(Map<PluginManifest, Plugin> plugins) {
        boolean modified = false;

        for (Map.Entry<PluginManifest, Plugin> entry : plugins.entrySet()) {
            if (this.register(entry.getKey(), entry.getValue())) {
                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean unregister(PluginManifest manifest) {
        Objects.requireNonNull(manifest, "manifest");

        if (this.plugins.containsKey(manifest)) {
            this.plugins.remove(manifest);

            return true;
        }

        return false;
    }

    @Override
    public boolean unregisterIf(Predicate<PluginManifest> condition) {
        boolean removed = false;
        Iterator<PluginManifest> iterator = this.plugins.keySet().iterator();

        while (iterator.hasNext()) {
            PluginManifest next = iterator.next();

            if (condition.test(next)) {
                iterator.remove();

                removed = this.unregisterDependenciesOf(next);
            }
        }

        return removed;
    }

    @Override
    public boolean unregisterAll(Collection<PluginManifest> manifests) {
        boolean removed = false;
        Iterator<PluginManifest> iterator = this.plugins.keySet().iterator();

        while (iterator.hasNext()) {
            PluginManifest next = iterator.next();

            if (manifests.contains(next)) {
                iterator.remove();

                removed = this.unregisterDependenciesOf(next);
            }
        }

        return removed;
    }

    private boolean unregisterDependenciesOf(PluginManifest pluginInformation) {
        Optional<Plugin> pluginResult = this.locator.find(pluginInformation);

        if (!pluginResult.isPresent()) {
            return false;
        }

        Plugin plugin = pluginResult.get();

        return this.unregisterAll(plugin.getDependencySystem()
                .getRegistry()
                .findAllDependencies()
                .keySet());
    }

    @Override
    public PluginLocatorService getLocator() {
        return this.locator;
    }

    @Override
    public Map<PluginManifest, Plugin> findAllPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }

    @Override
    public void clear() {
        this.plugins.clear();
    }

    private void clearDependencies() {
        this.findAllPlugins().values().forEach(plugin ->
                this.unregisterAll(plugin.getDependencySystem()
                        .getRegistry()
                        .findAllDependencies()
                        .keySet()));
    }
}