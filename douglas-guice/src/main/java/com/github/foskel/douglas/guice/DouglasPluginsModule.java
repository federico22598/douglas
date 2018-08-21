package com.github.foskel.douglas.guice;

import com.github.foskel.douglas.Douglas;
import com.github.foskel.douglas.instantiation.GuiceInstantiationStrategy;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.impl.StandardPluginManager;
import com.github.foskel.douglas.plugin.impl.load.DependencySatisfyingPluginLoadingListener;
import com.github.foskel.douglas.plugin.impl.load.StandardPluginLoader;
import com.github.foskel.douglas.plugin.impl.load.priority.AnnotationPluginPriorityResolver;
import com.github.foskel.douglas.plugin.impl.locate.SimplePluginLocatorProvider;
import com.github.foskel.douglas.plugin.impl.registry.StandardPluginRegistry;
import com.github.foskel.douglas.plugin.impl.resource.AnnotationResourceHandler;
import com.github.foskel.douglas.plugin.impl.scan.PathValidatingPluginScanningStrategy;
import com.github.foskel.douglas.plugin.impl.scan.validation.PathPluginSourceValidator;
import com.github.foskel.douglas.plugin.load.PluginLoader;
import com.github.foskel.douglas.plugin.load.PluginLoadingListener;
import com.github.foskel.douglas.plugin.load.priority.PluginPriorityResolver;
import com.github.foskel.douglas.plugin.locate.PluginLocatorProvider;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Provides Guice bindings for the module system
 *
 * @author Foskel
 */
public final class DouglasPluginsModule extends AbstractModule {

    @Provides
    static InstantiationStrategy<Plugin> providePluginInstantiationStrategy(Injector injector) {
        return new GuiceInstantiationStrategy<>(injector);
    }

    @Provides
    static List<PluginSourceValidator<Path>> providePluginSourceValidators() {
        return Collections.singletonList(new PathPluginSourceValidator());
    }

    @Provides
    static Collection<PluginLoadingListener> provideLoadingListeners() {
        return Collections.singletonList(
                new DependencySatisfyingPluginLoadingListener(
                        Collections.emptyList()));
    }

    @Override
    protected void configure() {
        this.bind(PluginManager.class)
                .to(StandardPluginManager.class)
                .in(Singleton.class);

        this.bind(PluginRegistry.class)
                .to(StandardPluginRegistry.class)
                .in(Singleton.class);

        this.bind(PluginScanningStrategy.class).to(PathValidatingPluginScanningStrategy.class);
        this.bind(PluginManifestExtractor.class).toInstance(Douglas.newPluginDescriptorExtractor());

        this.bind(ResourceHandler.class).to(AnnotationResourceHandler.class);
        this.bind(PluginPriorityResolver.class).to(AnnotationPluginPriorityResolver.class);

        this.bind(PluginLoader.class).to(StandardPluginLoader.class);
        this.bind(PluginLocatorProvider.class).to(SimplePluginLocatorProvider.class);
    }
}
