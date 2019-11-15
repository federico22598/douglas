package com.github.idkp.douglas.dagger;

import com.github.idkp.douglas.Douglas;
import com.github.idkp.douglas.instantiation.InstantiationStrategy;
import com.github.idkp.douglas.instantiation.ZeroArgumentInstantiationStrategy;
import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.PluginManager;
import com.github.idkp.douglas.plugin.impl.StandardPluginManager;
import com.github.idkp.douglas.plugin.impl.load.AnnotationPriorityResolver;
import com.github.idkp.douglas.plugin.impl.locate.SimplePluginLocatorProvider;
import com.github.idkp.douglas.plugin.impl.registry.StandardPluginRegistry;
import com.github.idkp.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.idkp.douglas.plugin.impl.scan.PathValidatingPluginScanningStrategy;
import com.github.idkp.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.idkp.douglas.plugin.load.PluginPriorityResolver;
import com.github.idkp.douglas.plugin.locate.PluginLocatorProvider;
import com.github.idkp.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.idkp.douglas.plugin.registry.PluginRegistry;
import com.github.idkp.douglas.plugin.resource.ResourceHandler;
import com.github.idkp.douglas.plugin.scan.PluginScanningStrategy;
import com.github.idkp.douglas.plugin.scan.validation.PluginSourceValidator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Module
public final class DouglasPluginsModule {

    @Provides
    @Singleton
    static PluginManager providePluginManager(StandardPluginManager pluginManager) {
        return pluginManager;
    }

    @Provides
    @Singleton
    static PluginRegistry providePluginRegistry(StandardPluginRegistry pluginRegistry) {
        return pluginRegistry;
    }

    @Provides
    static PluginScanningStrategy providePluginScanningStrategy(PathValidatingPluginScanningStrategy pluginScanningStrategy) {
        return pluginScanningStrategy;
    }

    @Provides
    static PluginPriorityResolver providePluginPriorityResolver() {
        return new AnnotationPriorityResolver();
    }

    @Provides
    static InstantiationStrategy<Plugin> providePluginInstantiationStrategy() {
        return new ZeroArgumentInstantiationStrategy<>();
    }

    @Provides
    static PluginManifestExtractor providePluginDescriptorExtractor() {
        return Douglas.newPluginDescriptorExtractor();
    }

    @Provides
    static List<PluginSourceValidator<Path>> providePluginSourceValidators() {
        return Collections.singletonList(new PathPluginSourceValidator());
    }

    @Provides
    static ResourceHandler provideResourceHandler() {
        return new AnnotationResourceHandler();
    }

    @Provides
    static PluginLocatorProvider providePluginLocatorServiceProvider() {
        return new SimplePluginLocatorProvider();
    }
}