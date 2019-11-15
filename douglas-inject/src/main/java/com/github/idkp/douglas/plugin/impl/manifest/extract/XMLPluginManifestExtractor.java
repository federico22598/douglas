package com.github.idkp.douglas.plugin.impl.manifest.extract;

import com.github.idkp.douglas.plugin.manifest.PluginManifest;
import com.github.idkp.douglas.plugin.manifest.extract.DataFileURLExtractor;
import com.github.idkp.douglas.plugin.manifest.extract.PluginDescriptorParser;
import com.github.idkp.douglas.plugin.manifest.extract.PluginManifestExtractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public final class XMLPluginManifestExtractor implements PluginManifestExtractor {
    private final DataFileURLExtractor<ClassLoader> dataFileURLExtractor;
    private final PluginDescriptorParser descriptorParser;
    private final String dataFilePath;

    public XMLPluginManifestExtractor(DataFileURLExtractor<ClassLoader> dataFileURLExtractor,
                                      PluginDescriptorParser descriptorParser,
                                      String dataFilePath) {
        this.dataFileURLExtractor = dataFileURLExtractor;
        this.descriptorParser = descriptorParser;
        this.dataFilePath = dataFilePath;
    }

    @Override
    public PluginManifest extract(Path pluginContainingFile) throws IOException {
        String pluginContainingFileName = pluginContainingFile.getFileName().toString();
        URL pluginContainingFileURL;

        try {
            pluginContainingFileURL = pluginContainingFile.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new IOException("Unable to of a valid ClassLoader for the " +
                    "candidate plugin JAR file \"" + pluginContainingFileName + "\":", e);
        }

        ClassLoader pluginContainingFileClassLoader = new URLClassLoader(new URL[]{
                pluginContainingFileURL});

        URL dataFileURL = this.dataFileURLExtractor.extract(
                pluginContainingFileClassLoader,
                this.dataFilePath,
                pluginContainingFile.getFileName().toString());

        return this.descriptorParser.parse(dataFileURL);
    }
}