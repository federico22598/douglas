package com.github.idkp.douglas.plugin.manifest.extract;

import com.github.idkp.douglas.plugin.manifest.PluginManifest;
import com.github.idkp.douglas.plugin.manifest.extract.exception.PluginDescriptorParsingException;

import java.net.URL;

public interface PluginDescriptorParser {
    PluginManifest parse(URL dataFileURL) throws PluginDescriptorParsingException;
}