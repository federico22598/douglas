package com.github.idkp.douglas.plugin.impl.scan;

import com.github.idkp.douglas.instantiation.InstantiationStrategy;
import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.douglas.plugin.manifest.PluginManifest;
import com.github.idkp.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.idkp.douglas.plugin.resource.ResourceHandler;
import com.github.idkp.douglas.plugin.scan.PluginScanFailedException;
import com.github.idkp.douglas.plugin.scan.PluginScanResult;
import com.github.idkp.douglas.plugin.scan.PluginScanningStrategy;
import com.github.idkp.douglas.plugin.scan.UnloadedPluginDependencyData;
import com.github.idkp.douglas.plugin.scan.validation.PluginSourceValidator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PathValidatingPluginScanningStrategy implements PluginScanningStrategy {
    private static final String REQUIRED_FILE_EXTENSION = ".jar";

    private final InstantiationStrategy<Plugin> instantiationStrategy;
    private final PluginManifestExtractor extractorService;
    private final List<PluginSourceValidator<Path>> pathValidators;
    private final ResourceHandler resourceHandler;
    //private final Map<PluginDescriptor, Queue<UnloadedPluginDependencyData>> pendingDependentPlugins;
    private final List<UnloadedPluginDependencyData> pendingDependentPlugins;
    private final List<PluginScanResult> currentScanResults;

    public PathValidatingPluginScanningStrategy(InstantiationStrategy<Plugin> instantiationStrategy,
                                                PluginManifestExtractor extractorService,
                                                List<PluginSourceValidator<Path>> pathValidators,
                                                ResourceHandler resourceHandler) {
        this.instantiationStrategy = instantiationStrategy;
        this.extractorService = extractorService;
        this.pathValidators = pathValidators;
        this.resourceHandler = resourceHandler;
        this.pendingDependentPlugins = new ArrayList<>();
        this.currentScanResults = new ArrayList<>();
    }

    private static boolean shouldLoadFile(Path file) {
        String fileName = file.getFileName().toString();

        return Files.isRegularFile(file) && fileName.endsWith(REQUIRED_FILE_EXTENSION);
    }

    @Override
    public Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException {
        currentScanResults.clear();

        this.validatePath(directory);

        Iterator<Path> pluginFiles;

        try {
            pluginFiles = Files.walk(directory).iterator();
        } catch (IOException e) {
            String directoryFileName = directory.getFileName().toString();

            throw new PluginScanFailedException("An IOException has been thrown while trying to" +
                    " scan for plugin files on the directory \"" + directoryFileName +
                    "\":", e);
        }

        URLPluginClassLoader classLoader = new URLPluginClassLoader(PathValidatingPluginScanningStrategy.class.getClassLoader());

        while (pluginFiles.hasNext()) {
            Path nextPluginFile = pluginFiles.next();

            if (!shouldLoadFile(nextPluginFile)) {
                continue;
            }

            PluginScanResult result = scanSingle(nextPluginFile, classLoader);
            List<PluginDescriptor> dependencyDescriptors = result.getPendingDependencyDescriptors();

            loadDependents(result.getManifest());

            if (!dependencyDescriptors.isEmpty()) {
                pendingDependentPlugins.add(new UnloadedPluginDependencyData(result.getManifest(), result.getScanWorker(), dependencyDescriptors.iterator()));
                continue;
            }

            currentScanResults.add(result);
        }

        return Collections.unmodifiableList(currentScanResults);
    }

    private void loadDependents(PluginManifest source) {
        Iterator<UnloadedPluginDependencyData> dependentIter = pendingDependentPlugins.iterator();

        while (dependentIter.hasNext()) {
            UnloadedPluginDependencyData dependent = dependentIter.next();
            Iterator<PluginDescriptor> descriptorIter = dependent.dependencyDescriptorIterator;

            while (descriptorIter.hasNext()) {
                PluginDescriptor nextDescriptor = descriptorIter.next();

                if (nextDescriptor.equals(source.getDescriptor())) {
                    descriptorIter.remove();

                    if (!descriptorIter.hasNext()) {
                        PluginScanResult result = dependent.scanWorker.scan(dependent.manifest);

                        currentScanResults.add(result);
                        dependentIter.remove();
                    }

                    break;
                }
            }
        }
    }

    @Override
    public PluginScanResult scanSingle(Path file) {
        ClassLoader parentClassLoader = PathValidatingPluginScanningStrategy.class.getClassLoader();

        return scanSingle(file, new URLPluginClassLoader(parentClassLoader));
    }

    private PluginScanResult scanSingle(Path file, URLPluginClassLoader classLoader) {
        try {
            classLoader.addURL(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new PluginScanFailedException("Unable to scan plugin JAR file " + file + ":", e);
        }

        PluginScanWorker scanWorker = new PluginScanWorker(this.instantiationStrategy,
                this.extractorService,
                this.resourceHandler,
                classLoader, file,
                this.currentScanResults);

        return scanWorker.scan();
    }

    private void validatePath(Path path) {
        this.pathValidators.forEach(validator -> validator.validate(path));
    }
}