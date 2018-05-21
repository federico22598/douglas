package com.github.foskel.douglas.plugin.impl.dependency.scan;

import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.extract.DependencyDescriptorExtractor;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.impl.dependency.scan.result.NullableDependencyScanResult;
import com.github.foskel.haptor.scan.DependencyScanResult;
import com.github.foskel.haptor.scan.DependencyScanningStrategy;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class FileDependencyScanningStrategy implements DependencyScanningStrategy<PluginDescriptor, Plugin> {
    private final DependencyDescriptorExtractor informationExtractorService;

    public FileDependencyScanningStrategy(DependencyDescriptorExtractor informationExtractorService) {
        this.informationExtractorService = informationExtractorService;
    }

    @Override
    public List<DependencyScanResult<PluginDescriptor, Plugin>> scan(Object source) {
        if (!(source instanceof Path)) {
            return Collections.emptyList();
        }

        Path file = (Path) source;
        Collection<PluginDescriptor> descriptors;

        try {
            descriptors = this.informationExtractorService.extract(file);
        } catch (IOException e) {
            e.printStackTrace();

            return Collections.emptyList();
        }

        return descriptors
                .stream()
                .map(NullableDependencyScanResult::new)
                .collect(Collectors.toList());
    }
}