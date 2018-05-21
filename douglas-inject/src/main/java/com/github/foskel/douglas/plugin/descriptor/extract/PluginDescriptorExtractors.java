package com.github.foskel.douglas.plugin.descriptor.extract;

public final class PluginDescriptorExtractors {
    private static final String STANDARD_CONFIGURATION_FILE_PATH = "META-INF/plugin.xml";

    private PluginDescriptorExtractors() {
    }

    public static String getStandardConfigurationFilePath() {
        return STANDARD_CONFIGURATION_FILE_PATH;
    }
}