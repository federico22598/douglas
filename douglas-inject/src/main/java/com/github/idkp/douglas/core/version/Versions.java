package com.github.idkp.douglas.core.version;

import com.github.idkp.douglas.core.version.parse.StandardVersionParser;
import com.github.idkp.douglas.core.version.parse.VersionParser;
import com.github.idkp.douglas.core.version.parse.VersionParsingException;

public final class Versions {
    private Versions() {
    }

    public static Version of(String versionAsString) throws VersionParsingException {
        VersionParser parser = StandardVersionParser.INSTANCE;

        return parser.parse(versionAsString);
    }
}
