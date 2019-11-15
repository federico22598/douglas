package com.github.idkp.douglas.plugin.manifest.extract;

import com.github.idkp.douglas.plugin.manifest.PluginManifest;

import java.io.IOException;
import java.nio.file.Path;

public interface PluginManifestExtractor {
    PluginManifest extract(Path pluginContainingFile) throws IOException;
}