package com.github.idkp.douglas.plugin.load;

import com.github.idkp.douglas.plugin.Plugin;

public interface PluginPriorityResolver {
    PluginPriority resolveLoading(Plugin plugin);

    PluginPriority resolveUnloading(Plugin plugin);
}
