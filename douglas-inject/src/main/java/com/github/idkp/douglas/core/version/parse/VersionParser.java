package com.github.idkp.douglas.core.version.parse;

import com.github.idkp.douglas.core.version.Version;

public interface VersionParser {
    Version parse(String input) throws VersionParsingException;
}
