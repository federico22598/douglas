package com.github.foskel.douglas;

import com.github.foskel.douglas.core.version.Tag;
import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.impl.manifest.extract.ClassLoaderDataFileURLExtractor;
import com.github.foskel.douglas.plugin.impl.manifest.extract.XMLPluginDescriptorParser;
import com.github.foskel.douglas.plugin.manifest.extract.PluginDescriptorExtractors;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractorBuilder;

/**
 * This class contains static factory methods to instantiate the main
 * classes needed to setup the API with the default configuration
 *
 * @author Foskel
 */
public final class Douglas {
    private static final Version VERSION = Version.builder()
            .major(0)
            .minor(2)
            .patch(2)
            .addTag(Tag.RELEASE)
            .build();

    private Douglas() {
    }

    public static Version getVersion() {
        return VERSION;
    }

    public static PluginManifestExtractor newPluginDescriptorExtractor() {
        return newPluginDescriptorExtractorBuilder()
                .withDataFileURLExtractor(new ClassLoaderDataFileURLExtractor())
                .withDescriptorParser(new XMLPluginDescriptorParser())
                .withDataFilePath(PluginDescriptorExtractors.getStandardConfigurationFilePath())
                .xml();
    }

    public static PluginManifestExtractorBuilder newPluginDescriptorExtractorBuilder() {
        return new PluginManifestExtractorBuilder();
    }
}