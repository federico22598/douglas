package com.github.foskel.douglas.plugin.impl.dependency.process;

import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.registry.PluginRegistry;
import com.github.foskel.haptor.process.DependencyProcessor;
import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
import com.github.foskel.haptor.satisfy.UnsatisfiedDependencyException;

public final class PluginRemovingDependencySatisfyingProcessor implements DependencyProcessor {
    private final PluginRegistry pluginRegistry;
    private final PluginDescriptor ownerPluginInformation;

    public PluginRemovingDependencySatisfyingProcessor(PluginRegistry pluginRegistry,
                                                       PluginDescriptor ownerPluginInformation) {
        this.pluginRegistry = pluginRegistry;
        this.ownerPluginInformation = ownerPluginInformation;
    }

    @Override
    public void postSatisfy(DependencySatisfyingResult result) throws UnsatisfiedDependencyException {
        if (!result.getValidationResult()) {
            PluginDescriptor dependencyIdentifier = (PluginDescriptor) result.getDependencyIdentifier();

            this.pluginRegistry.unregister(this.ownerPluginInformation);

            throw new UnsatisfiedDependencyException(
                    String.format("Unable to satisfy plugin dependency with name \"%s\" from \"%s\"",
                            dependencyIdentifier.getName(),
                            this.ownerPluginInformation.getName()));
        }
    }
}
