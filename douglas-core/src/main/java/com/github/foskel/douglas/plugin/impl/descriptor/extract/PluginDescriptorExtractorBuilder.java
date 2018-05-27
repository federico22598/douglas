package com.github.foskel.douglas.plugin.impl.descriptor.extract;

import com.github.foskel.douglas.plugin.descriptor.extract.DataFileURLExtractor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorParser;

import java.util.Objects;

public final class PluginDescriptorExtractorBuilder {
    private DataFileURLExtractor<ClassLoader> dataFileURLExtractor;
    private PluginDescriptorParser pluginDescriptorParser;
    private String dataFilePath;

    public PluginDescriptorExtractorBuilder withDataFileURLExtractor(DataFileURLExtractor<ClassLoader> dataFileURLExtractor) {
        Objects.requireNonNull(dataFileURLExtractor, "dataFileURLExtractor");

        this.dataFileURLExtractor = dataFileURLExtractor;

        return this;
    }

    public PluginDescriptorExtractorBuilder withDescriptorParser(PluginDescriptorParser pluginDescriptorParser) {
        Objects.requireNonNull(pluginDescriptorParser, "pluginDescriptorParser");

        this.pluginDescriptorParser = pluginDescriptorParser;

        return this;
    }

    public PluginDescriptorExtractorBuilder withDataFilePath(String dataFilePath) {
        Objects.requireNonNull(dataFilePath, "dataFilePath");

        this.dataFilePath = dataFilePath;

        return this;
    }

    public PluginDescriptorExtractor build() {
        if (this.dataFileURLExtractor == null
                || this.pluginDescriptorParser == null
                || this.dataFilePath == null) {
            throw new IllegalStateException("You must finish building the PluginDescriptorExtractor " +
                    "before calling PluginDescriptorExtractorBuilder#build!");
        }

        return new XMLPluginDescriptorExtractor(this.dataFileURLExtractor,
                this.pluginDescriptorParser,
                this.dataFilePath);
    }
}