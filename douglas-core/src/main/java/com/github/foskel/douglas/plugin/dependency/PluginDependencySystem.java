package com.github.foskel.douglas.plugin.dependency;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.haptor.DependencySystem;

/**
 * @author Foskel
 */
public interface PluginDependencySystem extends DependencySystem<PluginManifest, PluginManifest, Plugin> {
    void satisfy();

    <T extends Plugin> T find(String groupId,
                              String artifactId,
                              String version,
                              String name);
}