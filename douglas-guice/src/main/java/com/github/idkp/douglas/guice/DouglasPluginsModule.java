package com.github.idkp.douglas.guice;

import com.github.idkp.douglas.Douglas;
import com.github.idkp.douglas.instantiation.GuiceInstantiationStrategy;
import com.github.idkp.douglas.instantiation.InstantiationStrategy;
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
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public final class DouglasPluginsModule extends AbstractModule {

    @Provides
    static InstantiationStrategy<Plugin> providePluginInstantiationStrategy(Injector injector) {
        return new GuiceInstantiationStrategy<>(injector);
    }

    @Provides
    static List<PluginSourceValidator<Path>> providePluginSourceValidators() {
        return Collections.singletonList(new PathPluginSourceValidator());
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
        this.bind(PluginLocatorProvider.class).to(SimplePluginLocatorProvider.class);
        this.bind(PluginPriorityResolver.class).to(AnnotationPriorityResolver.class);
    }
}
