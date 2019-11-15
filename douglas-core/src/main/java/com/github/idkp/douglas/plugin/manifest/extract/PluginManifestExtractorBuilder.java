package com.github.idkp.douglas.plugin.manifest.extract;

import com.github.idkp.douglas.plugin.impl.manifest.extract.XMLPluginManifestExtractor;

import java.util.Objects;

public final class PluginManifestExtractorBuilder {
    private DataFileURLExtractor<ClassLoader> dataFileURLExtractor;
    private PluginDescriptorParser pluginDescriptorParser;
    private String dataFilePath;

    public PluginManifestExtractorBuilder withDataFileURLExtractor(DataFileURLExtractor<ClassLoader> dataFileURLExtractor) {
        Objects.requireNonNull(dataFileURLExtractor, "dataFileURLExtractor");

        this.dataFileURLExtractor = dataFileURLExtractor;

        return this;
    }

    public PluginManifestExtractorBuilder withDescriptorParser(PluginDescriptorParser pluginDescriptorParser) {
        Objects.requireNonNull(pluginDescriptorParser, "pluginDescriptorParser");

        this.pluginDescriptorParser = pluginDescriptorParser;

        return this;
    }

    public PluginManifestExtractorBuilder withDataFilePath(String dataFilePath) {
        Objects.requireNonNull(dataFilePath, "dataFilePath");

        this.dataFilePath = dataFilePath;

        return this;
    }

    public PluginManifestExtractor xml() {
        if (this.dataFileURLExtractor == null
                || this.pluginDescriptorParser == null
                || this.dataFilePath == null) {
            throw new IllegalStateException("You must finish building the PluginManifestExtractor " +
                    "before calling PluginManifestExtractorBuilder#xml!");
        }

        return new XMLPluginManifestExtractor(this.dataFileURLExtractor,
                this.pluginDescriptorParser,
                this.dataFilePath);
    }
}