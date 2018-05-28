package com.github.foskel.douglas.plugin.manifest.extract;

import com.github.foskel.douglas.plugin.manifest.extract.exception.MissingPluginDataFileException;

import java.net.URL;

public interface DataFileURLExtractor<S> {
    URL extract(S source,
                String dataFilePath,
                String sourceIdentifier) throws MissingPluginDataFileException;
}