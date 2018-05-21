package com.github.foskel.douglas.plugin.impl.load.priority;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.load.priority.PluginPriority;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;

import java.util.Objects;

public final class AnnotationPluginPriorityResolver implements PluginPriorityResolver {
    private static final PluginPriority DEFAULT_LOADING_PRIORITY = PluginPriority.NORMAL;
    private static final PluginPriority DEFAULT_UNLOADING_PRIORITY = PluginPriority.NORMAL;

    @Override
    public PluginPriority resolveLoadingPriority(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        Class<? extends Plugin> pluginType = plugin.getClass();

        if (!pluginType.isAnnotationPresent(Priority.class)) {
            return DEFAULT_LOADING_PRIORITY;
        }

        Priority annotation = pluginType.getAnnotation(Priority.class);

        return annotation.load();
    }

    @Override
    public PluginPriority resolveUnloadingPriority(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        Class<? extends Plugin> pluginType = plugin.getClass();

        if (!pluginType.isAnnotationPresent(Priority.class)) {
            return DEFAULT_UNLOADING_PRIORITY;
        }

        Priority annotation = pluginType.getAnnotation(Priority.class);

        return annotation.unload();
    }
}