package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.load.priority.AnnotationPluginPriorityResolver;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.priority.PluginPriority;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;

import javax.inject.Inject;
import java.util.*;

/**
 * @author Foskel
 */
public final class StandardPluginLoader implements PluginLoader {
    private List<Plugin> cachedPlugins;
    private final Comparator<Plugin> loadPriorityComparator;
    private final Comparator<Plugin> unloadPriorityComparator;

    @Inject
    StandardPluginLoader(PluginPriorityResolver priorityResolver) {
        this.loadPriorityComparator = new PluginLoadingPriorityComparator(priorityResolver);
        this.unloadPriorityComparator = new PluginUnloadingPriorityComparator(priorityResolver);
    }

    @Override
    public void load(Collection<Plugin> plugins) {
        if (!cachedPlugins.equals(plugins)) {
            cachedPlugins = new ArrayList<>(plugins);
        }

        cachedPlugins.sort(loadPriorityComparator);
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

        cachedPlugins.sort(unloadPriorityComparator);
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