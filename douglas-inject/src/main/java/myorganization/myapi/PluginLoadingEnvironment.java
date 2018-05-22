package myorganization.myapi;

import com.github.foskel.douglas.plugin.PluginManager;

import javax.inject.Inject;

public class PluginLoadingEnvironment {
    private final PluginManager pluginManager;

    @Inject
    public PluginLoadingEnvironment(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
}
