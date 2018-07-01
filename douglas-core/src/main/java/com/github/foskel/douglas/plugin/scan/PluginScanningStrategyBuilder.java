package com.github.foskel.douglas.plugin.scan;

import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.impl.scan.PathValidatingPluginScanningStrategy;
import com.github.foskel.douglas.plugin.manifest.extract.PluginManifestExtractor;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Foskel
 */
public final class PluginScanningStrategyBuilder {
    private final List<PluginSourceValidator<Path>> pathValidators = new ArrayList<>();
    private InstantiationStrategy<Plugin> instantiationStrategy;
    private PluginManifestExtractor pluginManifestExtractor;
    private ResourceHandler resourceHandler;

    public PluginScanningStrategyBuilder addPathValidator(PluginSourceValidator<Path> pathValidator) {
        Objects.requireNonNull(pathValidator, "pathValidator");

        this.pathValidators.add(pathValidator);

        return this;
    }

    public PluginScanningStrategyBuilder instantiationStrategy(InstantiationStrategy<Plugin> instantiationStrategy) {
        Objects.requireNonNull(instantiationStrategy, "instantiationStrategy");

        this.instantiationStrategy = instantiationStrategy;

        return this;
    }

    public PluginScanningStrategyBuilder informationExtractor(PluginManifestExtractor informationExtractor) {
        Objects.requireNonNull(informationExtractor, "informationExtractor");

        this.pluginManifestExtractor = informationExtractor;

        return this;
    }

    public PluginScanningStrategyBuilder resourceHandler(ResourceHandler resourceHandler) {
        Objects.requireNonNull(resourceHandler, "resourceHandler");

        this.resourceHandler = resourceHandler;

        return this;
    }

    public PluginScanningStrategy build() {
        if (this.instantiationStrategy == null || this.pluginManifestExtractor == null) {
            throw new IllegalStateException("You have to finish building the PluginScanningStrategy " +
                    "before calling PluginScanningStrategyBuilder#xml!");
        }

        return new PathValidatingPluginScanningStrategy(this.instantiationStrategy,
                this.pluginManifestExtractor,
                this.pathValidators,
                this.resourceHandler);
    }
}