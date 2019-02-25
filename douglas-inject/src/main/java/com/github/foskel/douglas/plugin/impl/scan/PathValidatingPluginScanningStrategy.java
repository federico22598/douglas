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
    private final List<PluginDescriptor> scannedDescriptors;

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
        this.scannedDescriptors = new ArrayList<>();
    }

    private static boolean shouldLoadFile(Path file) {
        String fileName = file.getFileName().toString();

        return Files.isRegularFile(file) && fileName.endsWith(REQUIRED_FILE_EXTENSION);
    }

    @Override
    public Collection<PluginScanResult> scan(Path directory) throws PluginScanFailedException {
        this.validatePath(directory);

        List<PluginScanResult> scanResults = new ArrayList<>();
        Iterator<Path> pluginFiles;

        try {
            pluginFiles = Files.walk(directory).iterator();
        } catch (IOException e) {
            String directoryFileName = directory.getFileName().toString();

            throw new PluginScanFailedException("An IOException has been thrown while trying to" +
                    " scan for plugin files on the directory \"" + directoryFileName +
                    "\":", e);
        }

        while (pluginFiles.hasNext()) {
            Path nextPluginFile = pluginFiles.next();

            if (!shouldLoadFile(nextPluginFile)) {
                continue;
            }

            PluginScanResult result = scanSingle(nextPluginFile);
            List<PluginDescriptor> dependencyDescriptors = result.getPendingDependencyDescriptors();

            loadDependents(result.getManifest(), scanResults);

            if (!dependencyDescriptors.isEmpty()) {
                for (PluginDescriptor descriptor : dependencyDescriptors) {
                    Queue<UnloadedPluginDependencyData> manifests = pendingDependentPlugins.get(descriptor);

                    if (manifests == null) {
                        manifests = new LinkedList<>();
                    }

                    manifests.add(new UnloadedPluginDependencyData(result.getManifest(), result.getScanWorker()));
                }

                continue;
            }

            scanResults.add(result);
        }

        return Collections.unmodifiableList(scanResults);
    }

    private void loadDependents(PluginManifest source, List<PluginScanResult> allResults) {
        Queue<UnloadedPluginDependencyData> dependenciesData = pendingDependentPlugins.get(source.getDescriptor());

        if (dependenciesData == null) {
            return;
        }

        while (!dependenciesData.isEmpty()) {
            UnloadedPluginDependencyData dependencyData = dependenciesData.poll();

            allResults.add(dependencyData.scanWorker.scan(dependencyData.manifest));
        }
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
                fileClassLoader, file,
                this.scannedDescriptors);

        return scanWorker.scan();
    }

    private void validatePath(Path path) {
        this.pathValidators.forEach(validator -> validator.validate(path));
    }
}