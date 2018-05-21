package com.github.foskel.douglas.plugin.impl.locate;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class StandardPluginLocatorService implements PluginLocatorService {
    private final Map<PluginDescriptor, Plugin> plugins;

    StandardPluginLocatorService(Map<PluginDescriptor, Plugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public Optional<Plugin> find(PluginDescriptor information) {
        return Optional.ofNullable(this.plugins.get(information));
    }

    @Override
    public Optional<Plugin> find(Predicate<PluginDescriptor> condition) {
        return this.plugins.entrySet()
                .stream()
                .filter(entry -> condition.test(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public Set<Plugin> findAll(Predicate<Plugin> condition) {
        return this.plugins.values()
                .stream()
                .filter(condition)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<PluginDescriptor, Plugin> findAllPlugins() {
        return Collections.unmodifiableMap(this.plugins);
    }
}