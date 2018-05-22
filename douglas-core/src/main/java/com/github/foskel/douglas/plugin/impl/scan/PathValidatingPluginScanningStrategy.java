package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanFailedException;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class PathValidatingPluginScanningStrategy implements PluginScanningStrategy {
    private final InstantiationStrategy<Plugin> instantiationStrategy;
    private final PluginDescriptorExtractor extractorService;
    private final List<PluginSourceValidator<Path>> pathValidators;
    private final ResourceHandler resourceHandler;

    public PathValidatingPluginScanningStrategy(InstantiationStrategy<Plugin> instantiationStrategy,
                                                PluginDescriptorExtractor extractorService,
                                                List<PluginSourceValidator<Path>> pathValidators,
                                                ResourceHandler resourceHandler) {
        this.instantiationStrategy = instantiationStrategy;
        this.extractorService = extractorService;
        this.pathValidators = pathValidators;
        this.resourceHandler = resourceHandler;
    }

    private static boolean shouldLoadFile(Path file) {
        String fileName = file.getFileName().toString();

        return Files.isRegularFile(file) && fileName.endsWith(".jar");
    }

    private static void addFileURLTo(URLPluginClassLoader parentClassLoader,
                                     Path file) {
        try {
            parentClassLoader.addURL(file.toUri().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static ClassLoader getClassLoaderFromFile(URLClassLoader parent,
                                                      Path file) {
        URL fileURL = Arrays.stream(parent.getURLs())
                .filter(url -> {
                    try {
                        return url.equals(file.toUri().toURL());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    return false;
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("The URLClassLoader " +
                        "\"" + parent.getClass().getSimpleName() + "\" does not contain " +
                        "any URL(s) for the Path \"" + file.getFileName().toString() + "\""));

        return new URLClassLoader(new URL[]{fileURL}, parent);
    }

    @Override
    public Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException {
        this.validatePath(directory);

        List<PluginScanResult> scanResults;
        URLPluginClassLoader directoryClassLoader = new URLPluginClassLoader(
                PathValidatingPluginScanningStrategy.class.getClassLoader());

        try {
            scanResults = Files.walk(directory)
                    .filter(PathValidatingPluginScanningStrategy::shouldLoadFile)
                    .peek(file -> addFileURLTo(directoryClassLoader, file))
                    .map(file -> this.scanSingle(file, directoryClassLoader))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            String directoryFileName = directory.getFileName().toString();

            throw new PluginScanFailedException("An IOException has been thrown while trying to" +
                    " scan for plugin files on the directory \"" + directoryFileName +
                    "\":", e);
        }

        return Collections.unmodifiableList(scanResults);
    }

    private PluginScanResult scanSingle(Path file, URLClassLoader classLoader) {
        ClassLoader fileClassLoader = getClassLoaderFromFile(classLoader, file);
        PluginScanWorker scanWorker = new PluginScanWorker(this.instantiationStrategy,
                this.extractorService,
                this.resourceHandler,
                fileClassLoader);

        return scanWorker.scan(file);
    }

    private void validatePath(Path path) {
        this.pathValidators.forEach(validator -> validator.validate(path));
    }
}