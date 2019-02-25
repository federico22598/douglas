package com.github.foskel.douglas.plugin.load;

import com.github.foskel.douglas.plugin.Plugin;

public interface PluginPriorityResolver {
    PluginPriority resolveLoading(Plugin plugin);

    PluginPriority resolveUnloading(Plugin plugin);
}
