package com.github.foskel.douglas.plugin.impl.load;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.load.priority.AnnotationPluginPriorityResolver;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * @author Foskel
 */
public final class StandardPluginLoader implements PluginLoader {
    private final Comparator<Plugin> loadPriorityComparator;
    private final Comparator<Plugin> unloadPriorityComparator;
    
    @Inject
    StandardPluginLoader(PluginPriorityResolver priorityResolver) {
        this.loadPriorityComparator = new PluginLoadingPriorityComparator(priorityResolver);
        this.unloadPriorityComparator = new PluginUnloadingPriorityComparator(priorityResolver);
    }

    @Override
    public void load(Collection<Plugin> plugins) {
        Plugin[] pluginsArr = plugins.toArray(new Plugin[plugins.size()]);

        Arrays.sort(pluginsArr, loadPriorityComparator);

        for (Plugin plugin : pluginsArr) {
            plugin.load();
        }
    }

    @Override
    public void unload(Collection<Plugin> plugins) {
        Plugin[] pluginsArr = plugins.toArray(new Plugin[plugins.size()]);

        Arrays.sort(pluginsArr, unloadPriorityComparator);

        for (Plugin plugin : pluginsArr) {
            plugin.load();
        }
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