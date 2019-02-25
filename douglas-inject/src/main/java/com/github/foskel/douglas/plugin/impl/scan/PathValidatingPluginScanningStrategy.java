package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanFailedException;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.douglas.plugin.scan.UnloadedPluginDependencyData;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Foskel
 */
public class PathValidatingPluginScanningStrategy implements PluginScanningStrategy {
    private static final String REQUIRED_FILE_EXTENSION = ".jar";

    private final InstantiationStrategy<Plugin> instantiationStrategy;
    private final PluginManifestExtractor extractorService;
    private final List<PluginSourceValidator<Path>> pathValidators;
    private final ResourceHandler resourceHandler;
    private final Map<PluginDescriptor, Queue<UnloadedPluginDependencyData>> pendingDependentPlugins;
    private final List<PluginScanResult> currentScanResults;

    @Inject
    PathValidatingPluginScanningStrategy(InstantiationStrategy<Plugin> instantiationStrategy,
                                         PluginManifestExtractor extractorService,
                                         List<PluginSourceValidator<Path>> pathValidators,
                                         ResourceHandler resourceHandler) {
        this.instantiationStrategy = instantiationStrategy;
        this.extractorService = extractorService;
        this.pathValidators = pathValidators;
        this.resourceHandler = resourceHandler;
        this.pendingDependentPlugins = new HashMap<>();
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

            System.out.println("[" + nextPluginFile.getFileName().toString() + "] Starting scan...");

            PluginScanResult result = scanSingle(nextPluginFile, classLoader);
            List<PluginDescriptor> dependencyDescriptors = result.getPendingDependencyDescriptors();

            System.out.println("[" + nextPluginFile.getFileName().toString() + "] Manifest: " + result.getManifest());
            System.out.println("[" + nextPluginFile.getFileName().toString() + "] Dependency descriptors: " + dependencyDescriptors);

            loadDependents(result.getManifest());

            if (!dependencyDescriptors.isEmpty()) {
                for (PluginDescriptor descriptor : dependencyDescriptors) {
                    Queue<UnloadedPluginDependencyData> manifests = pendingDependentPlugins.computeIfAbsent(descriptor,
                            __ -> new LinkedList<>());

                    manifests.add(new UnloadedPluginDependencyData(result.getManifest(), result.getScanWorker()));
                    System.out.println("[" + nextPluginFile.getFileName().toString() + "] Manifests for dependency descriptor: " + manifests);
                }

                continue;
            }

            currentScanResults.add(result);

            System.out.println("[" + nextPluginFile.getFileName().toString() + "] Added to results list.");
        }

        return Collections.unmodifiableList(currentScanResults);
    }

    private void loadDependents(PluginManifest source) {
        Queue<UnloadedPluginDependencyData> dependenciesData = pendingDependentPlugins.get(source.getDescriptor());

        if (dependenciesData == null) {
            return;
        }

        while (!dependenciesData.isEmpty()) {
            UnloadedPluginDependencyData dependencyData = dependenciesData.poll();

            currentScanResults.add(dependencyData.scanWorker.scan(dependencyData.manifest));
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