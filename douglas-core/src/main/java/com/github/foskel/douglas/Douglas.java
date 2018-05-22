package com.github.foskel.douglas;

import com.github.foskel.douglas.core.version.Tag;
import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.instantiation.ZeroArgumentInstantiationStrategy;
import com.github.foskel.douglas.module.Module;
import com.github.foskel.douglas.module.ModuleManager;
import com.github.foskel.douglas.module.dependency.SimpleModuleDependencySatisfyingService;
import com.github.foskel.douglas.module.io.JsonModulePropertyLoader;
import com.github.foskel.douglas.module.io.JsonModulePropertySaver;
import com.github.foskel.douglas.module.locate.SynchronizedModuleLocatorProvider;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractors;
import com.github.foskel.douglas.plugin.impl.StandardPluginManager;
import com.github.foskel.douglas.plugin.impl.dependency.extract.StandardDependencyDescriptorExtractor;
import com.github.foskel.douglas.plugin.impl.dependency.scan.FileDependencyScanningStrategy;
import com.github.foskel.douglas.plugin.impl.descriptor.extract.ClassLoaderDataFileURLExtractor;
import com.github.foskel.douglas.plugin.impl.descriptor.extract.PluginDescriptorExtractorBuilder;
import com.github.foskel.douglas.plugin.impl.descriptor.extract.XMLPluginDescriptorParser;
import com.github.foskel.douglas.plugin.impl.load.DependencySatisfyingPluginLoadingListener;
import com.github.foskel.douglas.plugin.impl.load.StandardPluginLoader;
import com.github.foskel.douglas.plugin.impl.locate.DependencyPluginLocatorServiceProvider;
import com.github.foskel.douglas.plugin.impl.registry.DependencyRemovingPluginRegistryDecorator;
import com.github.foskel.douglas.plugin.impl.registry.SimplePluginRegistry;
import com.github.foskel.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.foskel.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.haptor.scan.DependencyScanningStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public static PluginManager newPluginManager() {
        return newPluginManager(ZeroArgumentInstantiationStrategy.getInstance());
    }

    public static PluginManager newPluginManager(InstantiationStrategy<Plugin> instantiationStrategy) {
        List<PluginLoadingListener> loadingProcessors = Collections.singletonList(
                new DependencySatisfyingPluginLoadingListener(
                        Collections.emptyList()));

        return new StandardPluginManager(newPluginScanningStrategy(instantiationStrategy),
                newPluginRegistry(),
                StandardPluginLoader.INSTANCE,
                loadingProcessors);
    }

    public static PluginRegistry newPluginRegistry() {
        return new DependencyRemovingPluginRegistryDecorator(
                new SimplePluginRegistry(
                        DependencyPluginLocatorServiceProvider.INSTANCE));
    }

    public static PluginScanningStrategy newPluginScanningStrategy(InstantiationStrategy<Plugin> instantiationStrategy) {
        return PluginScanningStrategy.builder()
                .addPathValidator(PathPluginSourceValidator.INSTANCE)
                .instantiationStrategy(instantiationStrategy)
                .informationExtractor(newPluginDescriptorExtractor())
                .resourceHandler(new AnnotationResourceHandler())

                .build();
    }

    public static PluginDescriptorExtractor newPluginDescriptorExtractor() {
        return newPluginDescriptorExtractorBuilder()
                .withDataFileURLExtractor(ClassLoaderDataFileURLExtractor.INSTANCE)
                .withDescriptorParser(XMLPluginDescriptorParser.INSTANCE)
                .withDataFilePath(PluginDescriptorExtractors.getStandardConfigurationFilePath())

                .build();
    }

    public static PluginDescriptorExtractorBuilder newPluginDescriptorExtractorBuilder() {
        return new PluginDescriptorExtractorBuilder();
    }

    public static DependencyScanningStrategy<PluginDescriptor, Plugin> newDependencyScanningStrategy() {
        return new FileDependencyScanningStrategy(
                new StandardDependencyDescriptorExtractor(
                        newPluginDescriptorExtractor()));
    }

    public static ModuleManager newModuleManager() {
        return newModuleManager(Collections.emptyMap());
    }

    public static ModuleManager newModuleManager(Map<String, Module> modules) {
        return ModuleManager.builder(modules)
                .locatorProvider(new SynchronizedModuleLocatorProvider())
                .propertyLoader(new JsonModulePropertyLoader())
                .propertySaver(new JsonModulePropertySaver())
                .dependencySatisfier(new SimpleModuleDependencySatisfyingService())

                .build();
    }
}