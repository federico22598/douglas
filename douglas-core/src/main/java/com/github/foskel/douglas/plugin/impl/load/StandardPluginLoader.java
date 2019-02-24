package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.load.priority.AnnotationPluginPriorityResolver;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.priority.PluginPriority;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;

import java.util.*;

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
        if (!cachedPlugins.equals(plugins)) {
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
        if (!cachedPlugins.equals(plugins)) {
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
            PluginPriority firstPluginPriority = this.resolver.resolveLoadingPriority(firstPlugin);
            PluginPriority secondPluginPriority = this.resolver.resolveLoadingPriority(secondPlugin);

            return firstPluginPriority.compareTo(secondPluginPriority);
        }
    }

    private static class PluginUnloadingPriorityComparator implements Comparator<Plugin> {
        private final PluginPriorityResolver resolver;

        PluginUnloadingPriorityComparator(PluginPriorityResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public int compare(Plugin firstPlugin, Plugin secondPlugin) {
            PluginPriority firstPluginPriority = this.resolver.resolveUnloadingPriority(firstPlugin);
            PluginPriority secondPluginPriority = this.resolver.resolveUnloadingPriority(secondPlugin);

            return firstPluginPriority.compareTo(secondPluginPriority);
        }
    }
}