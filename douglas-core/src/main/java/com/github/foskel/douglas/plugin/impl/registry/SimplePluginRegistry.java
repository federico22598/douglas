package com.github.foskel.douglas.plugin.impl.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.locate.PluginLocatorServiceProvider;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class SimplePluginRegistry implements PluginRegistry {
    private final Map<PluginDescriptor, Plugin> plugins;
    private final PluginLocatorServiceProvider locatorServiceProvider;
    private PluginLocatorService locatorService;

    public SimplePluginRegistry(PluginLocatorServiceProvider locatorServiceProvider) {
        this.plugins = new ConcurrentHashMap<>();
        this.locatorServiceProvider = locatorServiceProvider;
    }

    @Override
    public boolean register(PluginDescriptor pluginInformation, Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        if (this.shouldRegister(pluginInformation)) {
            this.plugins.put(pluginInformation, plugin);

            return true;
        }

        return false;
    }

    private boolean shouldRegister(PluginDescriptor pluginInformation) {
        return !this.plugins.containsKey(pluginInformation);
    }

    @Override
    public boolean registerAll(Map<PluginDescriptor, Plugin> plugins) {
        return plugins.entrySet()
                .stream()
                .allMatch(entry -> this.register(entry.getKey(), entry.getValue()));
    }

    @Override
    public boolean unregister(PluginDescriptor pluginInformation) {
        Objects.requireNonNull(pluginInformation, "pluginInformation");

        if (this.plugins.containsKey(pluginInformation)) {
            this.plugins.remove(pluginInformation);

            return true;
        }

        return false;
    }

    @Override
    public boolean unregisterIf(Predicate<PluginDescriptor> condition) {
        return this.plugins.keySet().removeIf(condition);
    }

    @Override
    public boolean unregisterAll(Collection<PluginDescriptor> pluginInformationEntries) {
        return pluginInformationEntries
                .stream()
                .allMatch(this::unregister);
    }

    @Override
    public PluginLocatorService getLocator() {
        if (this.locatorService != null) {
            return this.locatorService;
        }

        this.locatorService = this.locatorServiceProvider.create(this.plugins);

        return this.locatorService;
    }

    @Override
    public Map<PluginDescriptor, Plugin> findAllPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }

    @Override
    public void clear() {
        this.plugins.clear();
    }
}