package com.github.foskel.douglas.plugin.load;

import com.github.foskel.douglas.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public final class PluginLoader {
    private final PluginPriorityResolver resolver;
    private final Comparator<Plugin> loadingComparator;
    private final Comparator<Plugin> unloadingComparator;

    public PluginLoader(PluginPriorityResolver resolver) {
        this.resolver = resolver;
        this.loadingComparator = Comparator.comparing(this.resolver::resolveLoading);
        this.unloadingComparator = Comparator.comparing(this.resolver::resolveUnloading);
    }

    public void load(Collection<Plugin> plugins) {
        Plugin[] pluginsArr = plugins.toArray(new Plugin[plugins.size()]);

        Arrays.sort(pluginsArr, loadingComparator);

        for (Plugin plugin : pluginsArr) {
            plugin.load();
        }
    }

    public void unload(Collection<Plugin> plugins) {
        Plugin[] pluginsArr = plugins.toArray(new Plugin[plugins.size()]);

        Arrays.sort(pluginsArr, unloadingComparator);

        for (Plugin plugin : pluginsArr) {
            plugin.unload();
        }
    }
}
