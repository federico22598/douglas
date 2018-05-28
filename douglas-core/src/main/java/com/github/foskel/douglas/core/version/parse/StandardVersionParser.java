package com.github.foskel.douglas.core.version.parse;

import com.github.foskel.douglas.core.version.Tag;
import com.github.foskel.douglas.core.version.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author http://blog.onyxbits.de/a-fast-java-parser-for-semantic-versioning-with-correct-precedence-ordering-380/
 */
public enum StandardVersionParser implements VersionParser {
    INSTANCE;

    private final List<String> tagNames = new ArrayList<>(5);
    private final List<String> metadata = new ArrayList<>(5);

    private final int[] versionNumbers = new int[3];
    private int errorInputIndex;
    private char[] input;

    @Override
    public Version parse(String input) throws VersionParsingException {
        this.input = input.toCharArray();

        if (!this.stateMajor()) { // Start recursive descend
            throw new VersionParsingException(input, this.errorInputIndex);
        }

        int majorVersion = this.versionNumbers[0];
        int minorVersion = this.versionNumbers[1];
        int patchVersion = this.versionNumbers[2];

        Set<Tag> tags = this.getTagsFromNames();

        return new Version(majorVersion,
                minorVersion,
                patchVersion,
                tags,
                this.metadata);
    }

    private Set<Tag> getTagsFromNames() {
        return this.tagNames.stream()
                .map(Tag::from)
                .map(tagResult -> tagResult.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private boolean stateMajor() {
        int pos = 0;

        while (pos < this.input.length && this.input[pos] >= '0' && this.input[pos] <= '9') {
            pos++; // match [0..9]+
        }

        if (pos == 0) { // Empty String -> Error
            return false;
        }

        this.versionNumbers[0] = Integer.parseInt(new String(this.input, 0, pos), 10);

        return this.input[pos] == '.' && stateMinor(pos + 1);

    }

    private boolean stateMinor(int index) {
        int pos = index;

        while (pos < this.input.length && this.input[pos] >= '0' && this.input[pos] <= '9') {
            pos++;// match [0..9]+
        }

        if (pos == index) { // Empty String -> Error
            this.errorInputIndex = index;

            return false;
        }

        this.versionNumbers[1] = Integer.parseInt(new String(this.input, index, pos - index), 10);

        if (this.input[pos] == '.') {
            return statePatch(pos + 1);
        }

        this.errorInputIndex = pos;

        return false;
    }

    private boolean statePatch(int index) {
        int pos = index;

        while (pos < this.input.length && this.input[pos] >= '0' && this.input[pos] <= '9') {
            pos++; // match [0..9]+
        }

        if (pos == index) { // Empty String -> Error
            this.errorInputIndex = index;

            return false;
        }

        this.versionNumbers[2] = Integer.parseInt(new String(this.input, index, pos - index), 10);

        if (pos == this.input.length) { // We have a clean version string
            return true;
        }

        if (this.input[pos] == '+') { // We have xml meta tagNames -> descend
            return stateMeta(pos + 1);
        }

        if (this.input[pos] == '-') { // We have pre release tagNames -> descend
            return stateRelease(pos + 1);
        }

        this.errorInputIndex = pos; // We have junk

        return false;
    }

    private boolean stateRelease(int index) {
        int pos = index;

        while ((pos < this.input.length)
                && ((this.input[pos] >= '0' && this.input[pos] <= '9')
                || (this.input[pos] >= 'a' && this.input[pos] <= 'z')
                || (this.input[pos] >= 'A' && this.input[pos] <= 'Z') || this.input[pos] == '-')) {
            pos++; // match [0..9a-zA-Z-]+
        }

        if (pos == index) { // Empty String -> Error
            this.errorInputIndex = index;

            return false;
        }

        this.tagNames.add(new String(this.input, index, pos - index));

        if (pos == this.input.length) { // End of this.input
            return true;
        }

        if (this.input[pos] == '.') { // More parts -> descend
            return stateRelease(pos + 1);
        }

        if (this.input[pos] == '+') { // Build meta -> descend
            return stateMeta(pos + 1);
        }

        this.errorInputIndex = pos;

        return false;
    }

    private boolean stateMeta(int index) {
        int pos = index;

        while ((pos < this.input.length)
                && ((this.input[pos] >= '0' && this.input[pos] <= '9')
                || (this.input[pos] >= 'a' && this.input[pos] <= 'z')
                || (this.input[pos] >= 'A' && this.input[pos] <= 'Z') || this.input[pos] == '-')) {
            pos++; // match [0..9a-zA-Z-]+
        }

        if (pos == index) { // Empty String -> Error
            this.errorInputIndex = index;
            return false;
        }

        this.metadata.add(new String(this.input, index, pos - index));

        if (pos == this.input.length) { // End of this.input
            return true;
        }

        if (this.input[pos] == '.') { // More parts -> descend
            return stateMeta(pos + 1);
        }

        this.errorInputIndex = pos;

        return false;
    }
}
