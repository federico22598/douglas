package com.github.foskel.douglas;

import com.github.foskel.douglas.core.version.Tag;
import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.instantiation.ZeroArgumentInstantiationStrategy;
import com.github.foskel.douglas.module.Module;
import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.SynchronizedModuleManager;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.impl.StandardPluginManager;
import com.github.foskel.douglas.plugin.impl.load.DependencySatisfyingListener;
import com.github.foskel.douglas.plugin.impl.manifest.extract.ClassLoaderDataFileURLExtractor;
import com.github.foskel.douglas.plugin.impl.manifest.extract.XMLPluginDescriptorParser;
import com.github.foskel.douglas.plugin.impl.registry.StandardPluginRegistry;
import com.github.foskel.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.foskel.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.manifest.extract.PluginDescriptorExtractors;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractorBuilder;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class contains static factory methods to instantiate the main
 * classes needed to setup the API with the default configuration
 *
 * @author Foskel
 */
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
        List<PluginLoadingListener> loadingProcessors = Collections.singletonList(
                new DependencySatisfyingListener(
                        Collections.emptyList()));

        return new StandardPluginManager(newPluginScanningStrategy(instantiationStrategy), newPluginRegistry(), loadingProcessors);
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