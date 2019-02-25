package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.instantiation.InstantiationException;
import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.dependency.PluginDependencyLocator;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanFailedException;
import com.github.foskel.douglas.plugin.scan.PluginScanResult;
import com.github.foskel.douglas.util.Exceptions;
import com.github.foskel.haptor.DependencyRef;
import com.github.foskel.haptor.DependencySystem;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ClassInfo;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Foskel
 */
public final class PluginScanWorker {
    private final InstantiationStrategy<Plugin> instantiationStrategy;
    private final PluginManifestExtractor extractorService;
    private final ResourceHandler resourceHandler;
    private final ClassLoader classLoader;
    private final List<PluginScanResult> allScanResults;
    private final Path file;

    public PluginScanWorker(InstantiationStrategy<Plugin> instantiationStrategy,
                            PluginManifestExtractor extractorService,
                            ResourceHandler resourceHandler,
                            ClassLoader classLoader, Path file,
                            List<PluginScanResult> allScanResults) {
        this.instantiationStrategy = instantiationStrategy;
        this.extractorService = extractorService;
        this.resourceHandler = resourceHandler;
        this.classLoader = classLoader;
        this.file = file;
        this.allScanResults = allScanResults;
    }

    private static void registerDependencies(Plugin plugin, PluginManifest manifest) {
        DependencySystem<PluginDescriptor, Plugin> dependencySystem = plugin.getDependencySystem();

        if (dependencySystem != null) {
            for (PluginDescriptor dependencyDescriptor : manifest.getDependencyDescriptors()) {
                dependencySystem.getRegistry().registerDirectly(dependencyDescriptor, null);//TODO: Make registerUnsatisfiedDirectly(...)
                dependencySystem.setCustomLocator(new PluginDependencyLocator(dependencySystem));
            }
        }
    }

    private List<PluginDescriptor> checkPendingDependencies(PluginManifest manifest) {
        Collection<PluginDescriptor> descriptors = manifest.getDependencyDescriptors();

        if (descriptors.isEmpty()) {
            return Collections.emptyList();
        }

        List<PluginDescriptor> result = new ArrayList<>();

        for (PluginDescriptor dependencyDescriptor : descriptors) {
            for (PluginScanResult lookupScanResult : allScanResults) {
                PluginDescriptor lookupDescriptor = lookupScanResult.getManifest().getDescriptor();

                if (lookupDescriptor.equals(dependencyDescriptor)) {
                    result.add(dependencyDescriptor);
                }
            }
        }

        return result;
    }

    public PluginScanResult scan() throws PluginScanFailedException {
        PluginManifest manifest;

        try {
            manifest = this.extractorService.extract(file);
        } catch (IOException e) {
            throw new PluginScanFailedException("Unable to scan '" + file + "':", e);
        }

        List<PluginDescriptor> pendingDependencies = checkPendingDependencies(manifest);

        if (!pendingDependencies.isEmpty()) {
            return new SimplePluginScanResult(manifest, pendingDependencies, this);
        }

        return scan(manifest);
    }

    public PluginScanResult scan(PluginManifest manifest) throws PluginScanFailedException {
        String mainClassCanonical = manifest.getMainClass();
        ScanResult result = new FastClasspathScanner(mainClassCanonical)
                .disableRecursiveScanning()
                .overrideClassLoaders(this.classLoader)
                .scan();

        Plugin plugin = null;

        for (ClassInfo info : result.getClassNameToClassInfo().values()) {
            Class<?> type = info.getClassRef();

            if (isPlugin(type, mainClassCanonical)) {
                this.handleResources(manifest, type);

                plugin = this.instantiate(type);
            }
        }

        if (plugin == null) {
            throw new PluginScanFailedException("Unable to find a valid plugin class which name matches \"" + mainClassCanonical + "\"");
        }

        System.out.println("Dependency descriptors: " + manifest.getDependencyDescriptors());
        registerDependencies(plugin, manifest);

        return new SimplePluginScanResult(manifest, plugin, this);
    }

    private static boolean isPlugin(Class<?> type, String requiredName) {
        return Plugin.class.isAssignableFrom(type) && type.getCanonicalName().equals(requiredName);
    }

    private void handleResources(PluginManifest descriptor, Class<?> type) {
        if (!descriptor.getResources().isEmpty()) {
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