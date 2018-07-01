package com.github.foskel.douglas.core.version;

import com.github.foskel.douglas.core.version.parse.StandardVersionParser;
import com.github.foskel.douglas.core.version.parse.VersionParser;
import com.github.foskel.douglas.core.version.parse.VersionParsingException;

/**
 * @author Foskel
 */
public final class Versions {
    private Versions() {
    }

    public static Version of(String versionAsString) throws VersionParsingException {
        VersionParser parser = StandardVersionParser.INSTANCE;

        return parser.parse(versionAsString);
    }
}
