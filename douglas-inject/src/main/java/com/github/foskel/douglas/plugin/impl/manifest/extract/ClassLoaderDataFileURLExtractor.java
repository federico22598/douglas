package com.github.foskel.douglas.plugin.impl.manifest.extract;

import com.github.foskel.douglas.plugin.manifest.extract.DataFileURLExtractor;
import com.github.foskel.douglas.plugin.manifest.extract.exception.MissingPluginDataFileException;

import java.net.URL;

/**
 * @author Foskel
 */
public final class ClassLoaderDataFileURLExtractor implements DataFileURLExtractor<ClassLoader> {

    @Override
    public URL extract(ClassLoader sourceClassLoader,
                       String dataFilePath,
                       String sourceIdentifier) throws MissingPluginDataFileException {
        URL dataFileURL = sourceClassLoader.getResource(dataFilePath);

        if (dataFileURL == null) {
            throw new MissingPluginDataFileException(
                    String.format("Missing required plugin information file (%s) for Plugin file \"%s\".",
                            dataFilePath,
                            sourceIdentifier));
        }

        return dataFileURL;
    }
}