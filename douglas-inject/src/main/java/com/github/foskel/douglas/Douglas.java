package com.github.foskel.douglas;

import com.github.foskel.douglas.core.version.Tag;
import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.plugin.dependency.PluginDependencySystem;
import com.github.foskel.douglas.plugin.impl.dependency.StandardPluginDependencySystem;
import com.github.foskel.douglas.plugin.impl.locate.SimplePluginLocatorProvider;
import com.github.foskel.douglas.plugin.impl.manifest.extract.ClassLoaderDataFileURLExtractor;
import com.github.foskel.douglas.plugin.impl.manifest.extract.XMLPluginDescriptorParser;
import com.github.foskel.douglas.plugin.manifest.extract.PluginDescriptorExtractors;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractorBuilder;

@SuppressWarnings("WeakerAccess")
public final class Douglas {
    private static final Version VERSION = Version.builder()
            .major(0)
            .minor(1)
            .patch(0)
            .addTag(Tag.RELEASE)

            .build();

    private Douglas() {
    }

    public static Version getVersion() {
        return VERSION;
    }

    public static PluginDependencySystem newPluginDependencySystem() {
        return new StandardPluginDependencySystem(new SimplePluginLocatorProvider());
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