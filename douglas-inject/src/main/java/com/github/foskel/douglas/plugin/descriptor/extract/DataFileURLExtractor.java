package com.github.foskel.douglas.plugin.descriptor.extract;

import com.github.foskel.douglas.plugin.descriptor.extract.exception.MissingPluginDataFileException;

import java.net.URL;

public interface DataFileURLExtractor<S> {
    URL extract(S source,
                String dataFilePath,
                String sourceIdentifier) throws MissingPluginDataFileException;
}