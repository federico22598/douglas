package com.github.foskel.douglas;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public final class Demo {
    private static final String TEST_PLUGIN_RESOURCE = "test_plugin.jar";

    private final PluginManager pluginManager = Douglas.newPluginManager();

    @Before
    public void loadTestPlugin() {
        Path pluginPath;

        try {
            pluginPath = Paths.get(Demo.class.getResource(TEST_PLUGIN_RESOURCE).toURI());
        } catch (URISyntaxException e) {
            return;
        }

        this.pluginManager.loadSingle(pluginPath);
    }

    @Test
    public void testPlugin_isRegistered() {
        Optional<Plugin> result = this.pluginManager.getRegistry()
                .getLocator()
                .find("com.github.foskel.douglas.plugins",
                        "test",
                        "1.0",
                        "Test");

        assertTrue(result.isPresent());
    }
}
