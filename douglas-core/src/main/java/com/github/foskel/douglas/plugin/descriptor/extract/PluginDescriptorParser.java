package com.github.foskel.douglas.plugin.descriptor.extract;

import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.descriptor.extract.exception.PluginDescriptorParsingException;

import java.net.URL;

public interface PluginDescriptorParser {
    PluginDescriptor parse(URL dataFileURL) throws PluginDescriptorParsingException;
}