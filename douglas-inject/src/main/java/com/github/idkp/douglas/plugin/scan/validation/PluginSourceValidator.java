package com.github.idkp.douglas.plugin.scan.validation;

public interface PluginSourceValidator<S> {
    void validate(S source) throws PluginSourceValidatingException;
}