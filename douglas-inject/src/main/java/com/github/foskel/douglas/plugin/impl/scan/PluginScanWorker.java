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
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

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

    private static void registerDependencies(Plugin plugin, Path file) {
        plugin.getDependencySystem().registerDependencies(file);
    }

    public PluginScanResult scan(Path file) throws PluginScanFailedException {
        ClassPath classPath;
        PluginDescriptor data;

        try {
            classPath = ClassPath.from(this.classLoader);
            data = this.extractorService.extract(file);
        } catch (IOException e) {
            throw new PluginScanFailedException(e.getMessage());
        }

        String packageId = data.getPackageId();
        String className = data.getMainClassName();

        Stream<ClassPath.ClassInfo> classes = classPath.getTopLevelClasses(packageId).stream();
        Plugin plugin = classes.filter(classInfo -> classInfo.getSimpleName().equals(className))
                .map(ClassPath.ClassInfo::load)
                .peek(type -> this.handleResources(data, type))
                .filter(Plugin.class::isAssignableFrom)
                .map(this::instantiate)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new PluginScanFailedException(
                        "Unable to find a valid plugin class which name matches " +
                                "\"" + className + "\" and package " +
                                "\"" + packageId + "\""));

        registerDependencies(plugin, file);

        return new SimplePluginScanResult(data, plugin);
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