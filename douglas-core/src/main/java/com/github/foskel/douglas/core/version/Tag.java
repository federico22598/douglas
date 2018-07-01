package com.github.foskel.douglas.core.version;

import com.github.foskel.douglas.core.traits.Named;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Foskel
 */
public enum Tag implements Named {
    SNAPSHOT("SNAPSHOT"),
    RELEASE("RELEASE"),
    RELEASE_CANDIDATE("RC"),
    ALPHA("ALPHA"),
    BETA("BETA");

    private final String name;

    Tag(String name) {
        this.name = name;
    }

    public static Optional<Tag> from(String name) {
        return Arrays.stream(values())
                .filter(tag -> tag.getName().equals(name))
                .findFirst();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}