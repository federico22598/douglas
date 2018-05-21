package com.github.foskel.douglas.plugin.impl.registry;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class DependencyRemovingPluginRegistryDecorator implements PluginRegistry {
    private final PluginRegistry backingDecoratedRegistry;

    @Inject
    DependencyRemovingPluginRegistryDecorator(PluginRegistry backingDecoratedRegistry) {
        this.backingDecoratedRegistry = backingDecoratedRegistry;
    }

    @Override
    public boolean register(PluginDescriptor pluginInformation,
                            Plugin plugin) {
        return this.backingDecoratedRegistry.register(pluginInformation, plugin);
    }

    @Override
    public boolean registerAll(Map<PluginDescriptor, Plugin> plugins) {
        return this.backingDecoratedRegistry.registerAll(plugins);
    }

    @Override
    public boolean unregister(PluginDescriptor pluginInformation) {
        return this.backingDecoratedRegistry.unregister(pluginInformation)
                && this.unregisterDependenciesFor(pluginInformation);
    }

    private boolean unregisterDependenciesFor(PluginDescriptor pluginInformation) {
        Optional<Plugin> pluginResult = this.backingDecoratedRegistry
                .getLocator()
                .find(pluginInformation);

        if (!pluginResult.isPresent()) {
            return false;
        }

        Plugin plugin = pluginResult.get();

        return plugin.getDependencySystem()
                .findAllDependencies()
                .keySet()
                .stream()
                .allMatch(this::unregister);
    }

    @Override
    public boolean unregisterIf(Predicate<PluginDescriptor> condition) {
        return this.backingDecoratedRegistry.unregisterIf(condition);
    }

    @Override
    public boolean unregisterAll(Collection<PluginDescriptor> pluginInformationEntries) {
        return this.backingDecoratedRegistry.unregisterAll(pluginInformationEntries);
    }

    @Override
    public PluginLocatorService getLocator() {
        return this.backingDecoratedRegistry.getLocator();
    }

    @Override
    public Map<PluginDescriptor, Plugin> findAllPlugins() {
        return this.backingDecoratedRegistry.findAllPlugins();
    }

    @Override
    public void clear() {
        this.unregisterAllDependencies();
        this.backingDecoratedRegistry.clear();
    }

    private void unregisterAllDependencies() {
        this.findAllPlugins().values()
                .stream()
                .map(Plugin::getDependencySystem)
                .map(PluginDependencySystem::findAllDependencies)
                .map(Map::keySet)
                .forEach(this::unregisterAll);
    }
}
