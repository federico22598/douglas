package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.load.priority.AnnotationPluginPriorityResolver;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author Foskel
 */
public enum StandardPluginLoader implements PluginLoader {
    INSTANCE;

    private static final Comparator<Plugin> LOAD_PRIORITY_COMPARATOR = new PluginLoadingPriorityComparator(AnnotationPluginPriorityResolver.INSTANCE);
    private static final Comparator<Plugin> UNLOAD_PRIORITY_COMPARATOR = new PluginUnloadingPriorityComparator(AnnotationPluginPriorityResolver.INSTANCE);
    private List<Plugin> cachedPlugins;

    @Override
    public void load(Collection<Plugin> plugins) {
        if (cachedPlugins == null || !cachedPlugins.equals(plugins)) {
            cachedPlugins = new ArrayList<>(plugins);
        }

        cachedPlugins.sort(LOAD_PRIORITY_COMPARATOR);
        cachedPlugins.forEach(Plugin::load);
        /*plugins.stream()
                .sorted(LOAD_PRIORITY_COMPARATOR)
                .forEach(Plugin::load);*/
    }

    @Override
    public void unload(Collection<Plugin> plugins) {
        if (cachedPlugins == null || !cachedPlugins.equals(plugins)) {
            cachedPlugins = new ArrayList<>(plugins);
        }

        cachedPlugins.sort(UNLOAD_PRIORITY_COMPARATOR);
        cachedPlugins.forEach(Plugin::unload);
    }

    private static class PluginLoadingPriorityComparator implements Comparator<Plugin> {
        private final PluginPriorityResolver resolver;

        PluginLoadingPriorityComparator(PluginPriorityResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public int compare(Plugin firstPlugin, Plugin secondPlugin) {
            return this.resolver.resolveLoadingPriority(firstPlugin).compareTo(
                    this.resolver.resolveLoadingPriority(secondPlugin));
        }
    }

    private static class PluginUnloadingPriorityComparator implements Comparator<Plugin> {
        private final PluginPriorityResolver resolver;

        PluginUnloadingPriorityComparator(PluginPriorityResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public int compare(Plugin firstPlugin, Plugin secondPlugin) {
            return this.resolver.resolveUnloadingPriority(firstPlugin).compareTo(
                    this.resolver.resolveUnloadingPriority(secondPlugin));
        }
    }
}