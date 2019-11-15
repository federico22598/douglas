package com.github.idkp.douglas.plugin.impl.dependency;

import com.github.idkp.douglas.plugin.manifest.PluginDescriptor;
import com.github.idkp.douglas.plugin.manifest.PluginManifest;
import com.github.idkp.douglas.plugin.registry.PluginRegistry;
import com.github.idkp.haptor.process.DependencyProcessor;
import com.github.idkp.haptor.satisfy.DependencySatisfyingResult;
import com.github.idkp.haptor.satisfy.UnsatisfiedDependencyException;

public final class PluginRemovingProcessor implements DependencyProcessor {
    private final PluginRegistry pluginRegistry;
    private final PluginManifest ownerPluginInformation;

    public PluginRemovingProcessor(PluginRegistry pluginRegistry, PluginManifest ownerPluginInformation) {
        this.pluginRegistry = pluginRegistry;
        this.ownerPluginInformation = ownerPluginInformation;
    }

    @Override
    public void postSatisfy(DependencySatisfyingResult result) throws UnsatisfiedDependencyException {
        if (!result.getValidationResult()) {
            this.pluginRegistry.unregister(this.ownerPluginInformation);

            PluginDescriptor descriptor = (PluginDescriptor) result.getIdentifier();

            throw new UnsatisfiedDependencyException(
                    String.format("Unable to satisfy plugin dependency with name \"%s\" from \"%s\"",
                            descriptor.getName(),
                            this.ownerPluginInformation.getDescriptor().getName()));
        }
    }
}
