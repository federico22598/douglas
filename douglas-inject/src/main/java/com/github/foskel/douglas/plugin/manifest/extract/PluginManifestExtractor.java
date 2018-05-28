package com.github.foskel.douglas.plugin.manifest.extract;

import com.github.foskel.douglas.plugin.manifest.PluginManifest;

import java.io.IOException;
import java.nio.file.Path;

public interface PluginManifestExtractor {
    PluginManifest extract(Path pluginContainingFile) throws IOException;
}