package com.github.foskel.douglas.plugin.impl.scan.validation;

import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidatingException;
import com.github.foskel.douglas.plugin.scan.validation.PluginSourceValidator;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Foskel
 */
public enum PathPluginSourceValidator implements PluginSourceValidator<Path> {
    INSTANCE;


    @Override
    public void validate(Path path) throws PluginSourceValidatingException {
        if (path == null) {
            throw new PluginSourceValidatingException("The Path cannot be null!");
        } else if (!Files.isDirectory(path)) {
            throw new PluginSourceValidatingException("The provided Path (" + path
                    .getFileName()
                    .toString() + ") is not a directory");
        }
    }
}