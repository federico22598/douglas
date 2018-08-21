package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanFailedException;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Foskel
 */
public class PathValidatingPluginScanningStrategy implements PluginScanningStrategy {
    private static final String REQUIRED_FILE_EXTENSION = ".jar";

    private final InstantiationStrategy<Plugin> instantiationStrategy;
    private final PluginManifestExtractor extractorService;
    private final List<PluginSourceValidator<Path>> pathValidators;
    private final ResourceHandler resourceHandler;

    public PathValidatingPluginScanningStrategy(InstantiationStrategy<Plugin> instantiationStrategy,
                                                PluginManifestExtractor extractorService,
                                                List<PluginSourceValidator<Path>> pathValidators,
                                                ResourceHandler resourceHandler) {
        this.instantiationStrategy = instantiationStrategy;
        this.extractorService = extractorService;
        this.pathValidators = pathValidators;
        this.resourceHandler = resourceHandler;
    }

    private static boolean shouldLoadFile(Path file) {
        String fileName = file.getFileName().toString();

        return Files.isRegularFile(file) && fileName.endsWith(REQUIRED_FILE_EXTENSION);
    }

    @Override
    public Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException {
        this.validatePath(directory);

        List<PluginScanResult> scanResults;

        try {
            scanResults = Files.walk(directory)
                    .filter(PathValidatingPluginScanningStrategy::shouldLoadFile)
                    .map(this::scanSingle)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            String directoryFileName = directory.getFileName().toString();

            throw new PluginScanFailedException("An IOException has been thrown while trying to" +
                    " scan for plugin files on the directory \"" + directoryFileName +
                    "\":", e);
        }

        return Collections.unmodifiableList(scanResults);
    }

    @Override
    public PluginScanResult scanSingle(Path file) {
        ClassLoader parentClassLoader = PathValidatingPluginScanningStrategy.class.getClassLoader();
        ClassLoader fileClassLoader;

        try {
            fileClassLoader = new URLPluginClassLoader(new URL[]{file.toUri().toURL()}, parentClassLoader);
        } catch (MalformedURLException e) {
            throw new PluginScanFailedException("Unable to scan plugin JAR file " + file + ":", e);
        }

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