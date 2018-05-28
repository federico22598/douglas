package com.github.foskel.douglas.plugin.manifest.extract;

import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.manifest.extract.exception.PluginDescriptorParsingException;

import java.net.URL;

public interface PluginDescriptorParser {
    PluginManifest parse(URL dataFileURL) throws PluginDescriptorParsingException;
}