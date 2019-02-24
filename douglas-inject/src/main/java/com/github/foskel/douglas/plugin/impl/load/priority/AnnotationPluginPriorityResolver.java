package com.github.foskel.douglas.plugin.impl.load.priority;

import com.github.foskel.douglas.annotations.Priority;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.load.priority.PluginPriority;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;

import java.util.Objects;

/**
 * @author Foskel
 */
public final class AnnotationPluginPriorityResolver implements PluginPriorityResolver {
    private static final PluginPriority DEFAULT_PRIORITY = PluginPriority.NORMAL;

    @Override
    public PluginPriority resolveLoadingPriority(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        Class<? extends Plugin> pluginType = plugin.getClass();

        if (!pluginType.isAnnotationPresent(Priority.class)) {
            return DEFAULT_PRIORITY;
        }

        Priority annotation = pluginType.getAnnotation(Priority.class);

        return annotation.load();
    }

    @Override
    public PluginPriority resolveUnloadingPriority(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        Class<? extends Plugin> pluginType = plugin.getClass();

        if (!pluginType.isAnnotationPresent(Priority.class)) {
            return DEFAULT_PRIORITY;
        }

        Priority annotation = pluginType.getAnnotation(Priority.class);

        return annotation.unload();
    }
}