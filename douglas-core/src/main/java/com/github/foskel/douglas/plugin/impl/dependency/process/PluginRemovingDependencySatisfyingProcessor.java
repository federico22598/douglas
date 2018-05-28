package com.github.foskel.douglas.plugin.impl.dependency.process;

import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;
import com.github.foskel.douglas.plugin.manifest.PluginManifest;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;

public final class PluginRemovingDependencySatisfyingProcessor implements DependencyProcessor {
    private final PluginRegistry pluginRegistry;
    private final PluginManifest ownerPluginInformation;

    public PluginRemovingDependencySatisfyingProcessor(PluginRegistry pluginRegistry,
                                                       PluginManifest ownerPluginInformation) {
        this.pluginRegistry = pluginRegistry;
        this.ownerPluginInformation = ownerPluginInformation;
    }

    @Override
    public void postSatisfy(DependencySatisfyingResult result) throws UnsatisfiedDependencyException {
        if (!result.getValidationResult()) {
            PluginManifest dependencyIdentifier = (PluginManifest) result.getDependencyIdentifier();
            PluginDescriptor descriptor = dependencyIdentifier.getDescriptor();

            this.pluginRegistry.unregister(this.ownerPluginInformation);

            throw new UnsatisfiedDependencyException(
                    String.format("Unable to satisfy plugin dependency with name \"%s\" from \"%s\"",
                            descriptor.getName(),
                            this.ownerPluginInformation.getDescriptor().getName()));
        }
    }
}
