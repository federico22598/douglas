package com.github.foskel.douglas.dagger;

import com.github.foskel.douglas.Douglas;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.instantiation.ZeroArgumentInstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;
import com.github.foskel.douglas.plugin.impl.StandardPluginManager;
import com.github.foskel.douglas.plugin.impl.load.DependencySatisfyingPluginLoadingListener;
import com.github.foskel.douglas.plugin.impl.load.StandardPluginLoader;
import com.github.foskel.douglas.plugin.impl.load.priority.AnnotationPluginPriorityResolver;
import com.github.foskel.douglas.plugin.impl.locate.DependencyPluginLocatorServiceProvider;
import com.github.foskel.douglas.plugin.impl.registry.SimplePluginRegistry;
import com.github.foskel.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.foskel.douglas.plugin.impl.scan.PathValidatingPluginScanningStrategy;
import com.github.foskel.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;
import com.github.foskel.douglas.plugin.locate.PluginLocatorServiceProvider;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Module
public final class PluginsModule {

    @Provides
    @Singleton
    static PluginManager providePluginManager(StandardPluginManager pluginManager) {
        return pluginManager;
    }

    @Provides
    @Singleton
    static PluginRegistry providePluginRegistry(SimplePluginRegistry pluginRegistry) {
        return pluginRegistry;
    }

    @Provides
    static PluginScanningStrategy providePluginScanningStrategy(PathValidatingPluginScanningStrategy pluginScanningStrategy) {
        return pluginScanningStrategy;
    }

    @Provides
    static InstantiationStrategy<Plugin> providePluginInstantiationStrategy() {
        return new ZeroArgumentInstantiationStrategy<>();
    }

    @Provides
    static PluginDescriptorExtractor providePluginDescriptorExtractor() {
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
    static PluginPriorityResolver providePluginPriorityResolver() {
        return new AnnotationPluginPriorityResolver();
    }

    @Provides
    static PluginLoader providePluginLoader(StandardPluginLoader pluginLoader) {
        return pluginLoader;
    }

    @Provides
    static Collection<PluginLoadingListener> provideLoadingListeners() {
        return Collections.singletonList(
                new DependencySatisfyingPluginLoadingListener(
                        Collections.emptyList()));
    }

    @Provides
    static PluginLocatorServiceProvider providePluginLocatorServiceProvider() {
        return new DependencyPluginLocatorServiceProvider();
    }
}