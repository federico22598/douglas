package com.github.foskel.douglas.core.version.parse;

import com.github.foskel.douglas.core.version.Version;

public interface VersionParser {
    Version parse(String input) throws VersionParsingException;
}
