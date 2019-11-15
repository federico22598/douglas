package com.github.idkp.douglas;

import com.github.idkp.douglas.core.version.Tag;
import com.github.idkp.douglas.core.version.Version;
import com.github.idkp.douglas.instantiation.InstantiationStrategy;
import com.github.idkp.douglas.instantiation.ZeroArgumentInstantiationStrategy;
import com.github.idkp.douglas.module.Module;
import com.github.idkp.douglas.module.ModuleManager;
import com.github.idkp.douglas.module.SynchronizedModuleManager;
import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.PluginManager;
import com.github.idkp.douglas.plugin.impl.StandardPluginManager;
import com.github.idkp.douglas.plugin.impl.load.AnnotationPriorityResolver;
import com.github.idkp.douglas.plugin.impl.manifest.extract.ClassLoaderDataFileURLExtractor;
import com.github.idkp.douglas.plugin.impl.manifest.extract.XMLPluginDescriptorParser;
import com.github.idkp.douglas.plugin.impl.registry.StandardPluginRegistry;
import com.github.idkp.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.idkp.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.idkp.douglas.plugin.manifest.extract.PluginDescriptorExtractors;
import com.github.idkp.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.idkp.douglas.plugin.manifest.extract.PluginManifestExtractorBuilder;
import com.github.idkp.douglas.plugin.registry.PluginRegistry;
import com.github.idkp.douglas.plugin.scan.PluginScanningStrategy;

import java.util.Collections;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
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

    public static PluginManager newPluginManager() {
        return newPluginManager(ZeroArgumentInstantiationStrategy.getInstance());
    }

    public static PluginManager newPluginManager(InstantiationStrategy<Plugin> instantiationStrategy) {
        return new StandardPluginManager(newPluginScanningStrategy(instantiationStrategy), newPluginRegistry(), new AnnotationPriorityResolver());
    }

    public static PluginRegistry newPluginRegistry() {
        return new StandardPluginRegistry();
    }

    public static PluginScanningStrategy newPluginScanningStrategy(InstantiationStrategy<Plugin> instantiationStrategy) {
        return PluginScanningStrategy.builder()
                .addPathValidator(PathPluginSourceValidator.INSTANCE)
                .instantiationStrategy(instantiationStrategy)
                .informationExtractor(newPluginDescriptorExtractor())
                .resourceHandler(new AnnotationResourceHandler())
                .build();
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

    public static ModuleManager newModuleManager() {
        return newModuleManager(Collections.emptyMap());
    }

    public static ModuleManager newModuleManager(Map<String, Module> modules) {
        return new SynchronizedModuleManager(modules);
    }
}