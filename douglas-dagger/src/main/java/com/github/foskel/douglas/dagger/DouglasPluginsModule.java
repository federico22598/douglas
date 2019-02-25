package com.github.foskel.douglas.dagger;

import com.github.foskel.douglas.Douglas;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.instantiation.ZeroArgumentInstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.impl.StandardPluginManager;
import com.github.foskel.douglas.plugin.impl.load.DependencySatisfyingListener;
import com.github.foskel.douglas.plugin.impl.locate.SimplePluginLocatorProvider;
import com.github.foskel.douglas.plugin.impl.registry.StandardPluginRegistry;
import com.github.foskel.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.foskel.douglas.plugin.impl.scan.PathValidatingPluginScanningStrategy;
import com.github.foskel.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
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

/**
 * Provides Dagger bindings for the plugin system
 *
 * @author Foskel
 */
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
    static Collection<PluginLoadingListener> provideLoadingListeners() {
        return Collections.singletonList(
                new DependencySatisfyingListener(
                        Collections.emptyList()));
    }

    @Provides
    static PluginLocatorProvider providePluginLocatorServiceProvider() {
        return new SimplePluginLocatorProvider();
    }
}