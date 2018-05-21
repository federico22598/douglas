package com.github.foskel.douglas.plugin.impl.scan;

import com.github.foskel.douglas.instantiation.InstantiationStrategy;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorExtractor;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;
import com.github.foskel.douglas.plugin.scan.PluginScanningStrategy;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PluginScanningStrategyBuilder {
    private final List<PluginSourceValidator<Path>> pathValidators = new ArrayList<>();
    private InstantiationStrategy<Plugin> instantiationStrategy;
    private PluginDescriptorExtractor pluginDescriptorExtractor;
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

    public PluginScanningStrategyBuilder informationExtractor(PluginDescriptorExtractor informationExtractor) {
        Objects.requireNonNull(informationExtractor, "informationExtractor");

        this.pluginDescriptorExtractor = informationExtractor;

        return this;
    }

    public PluginScanningStrategyBuilder resourceHandler(ResourceHandler resourceHandler) {
        Objects.requireNonNull(resourceHandler, "resourceHandler");

        this.resourceHandler = resourceHandler;

        return this;
    }

    public PluginScanningStrategy build() {
        if (this.instantiationStrategy == null || this.pluginDescriptorExtractor == null) {
            throw new IllegalStateException("You have to finish building the PluginScanningStrategy " +
                    "before calling PluginScanningStrategyBuilder#build!");
        }

        return new PathValidatingPluginScanningStrategy(this.instantiationStrategy,
                this.pluginDescriptorExtractor,
                this.pathValidators,
                this.resourceHandler);
    }
}