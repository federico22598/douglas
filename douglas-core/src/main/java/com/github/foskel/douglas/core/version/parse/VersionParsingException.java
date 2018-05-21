package com.github.foskel.douglas.core.version.parse;

import java.text.ParseException;

public class VersionParsingException extends ParseException {
    VersionParsingException(String message, int errorOffset) {
        super(message, errorOffset);
    }
}