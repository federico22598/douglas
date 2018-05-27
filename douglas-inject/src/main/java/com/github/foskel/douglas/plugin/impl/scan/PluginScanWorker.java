package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.instantiation.InstantiationException;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;
import com.github.foskel.douglas.plugin.impl.scan.result.SimplePluginScanResult;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanFailedException;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.util.Exceptions;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public final class PluginScanWorker {
    private final InstantiationStrategy<Plugin> instantiationStrategy;
    private final PluginDescriptorExtractor extractorService;
    private final ResourceHandler resourceHandler;
    private final ClassLoader classLoader;

    public PluginScanWorker(InstantiationStrategy<Plugin> instantiationStrategy,
                            PluginDescriptorExtractor extractorService,
                            ResourceHandler resourceHandler,
                            ClassLoader classLoader) {
        this.instantiationStrategy = instantiationStrategy;
        this.extractorService = extractorService;
        this.resourceHandler = resourceHandler;
        this.classLoader = classLoader;
    }

    public PluginScanResult scan(Path file) throws PluginScanFailedException {
        PluginDescriptor descriptor;

        try {
            descriptor = this.extractorService.extract(file);
        } catch (IOException e) {
            throw new PluginScanFailedException("Unable to scan '" + file + "':", e);
        }

        String mainClass = descriptor.getMainClass();
        AtomicReference<Plugin> pluginHolder = new AtomicReference<>();

        new FastClasspathScanner(mainClass)
                .addClassLoader(this.classLoader)
                .matchClassesImplementing(Plugin.class, type -> {
                    this.handleResources(descriptor, type);
                    pluginHolder.set(this.instantiate(type));
                })
                .scan();

        Plugin plugin = pluginHolder.get();

        if (plugin == null) {
            throw new PluginScanFailedException("Unable to find a valid plugin class which name matches \"" + mainClass + "\"");
        }

        registerDependencies(plugin, descriptor);

        return new SimplePluginScanResult(descriptor, plugin);
    }

    private static void registerDependencies(Plugin plugin, PluginDescriptor descriptor) {
        plugin.getDependencySystem().register(descriptor);
    }

    private void handleResources(PluginDescriptor descriptor,
                                 Class<?> type) {
        if (descriptor.getResourceTargets().contains(type.getCanonicalName())) {
            this.resourceHandler.handle(type, this.classLoader);
        }
    }

    @SuppressWarnings("unchecked")
    private Plugin instantiate(Class<?> type) {
        try {
            return this.instantiationStrategy.instantiate((Class<? extends Plugin>) type, this.classLoader);
        } catch (InstantiationException e) {
            Exceptions.throwAsUnchecked(e);
        }

        return null;
    }
}