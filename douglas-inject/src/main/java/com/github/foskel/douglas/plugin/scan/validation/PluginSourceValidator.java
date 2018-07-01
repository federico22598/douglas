package com.github.foskel.douglas.plugin.scan.validation;

/**
 * @author Foskel
 */
public interface PluginSourceValidator<S> {
    void validate(S source) throws PluginSourceValidatingException;
}