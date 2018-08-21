package com.github.foskel.douglas.plugin;

import com.github.foskel.douglas.Douglas;
import org.junit.Test;

import java.nio.file.Paths;

public final class PluginsDemo {
    private static final String PLUGINS_DIR_PATH = "C:\\Users\\F\\Documents\\douglas-plugins";
    private final PluginManager pluginManager = Douglas.newPluginManager();

    @Test
    public void loadPlugins() {
        pluginManager.load(Paths.get(PLUGINS_DIR_PATH));
    }
}
