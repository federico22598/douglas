package com.github.idkp.douglas.core.version;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 5/9/2017
 */
public final class Version implements Comparable<Version> {
    public static final Version INITIAL_VERSION = new Version(0, 1, 0);
    public static final Version MIN_VERSION = new Version(0, 0, 0);
    public static final Version MAX_VERSION = new Version(Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE);

    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;

    private final Set<Tag> tags;
    private final List<String> metadata;

    public Version(int majorVersion,
                   int minorVersion,
                   int patchVersion,
                   Set<Tag> tags,
                   List<String> metadata) {
        this.validate(majorVersion,
                minorVersion,
                patchVersion,
                tags);

        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
        this.tags = tags;
        this.metadata = metadata;
    }

    public Version(int majorVersion,
                   int minorVersion,
                   int patchVersion) {
        this(majorVersion,
                minorVersion,
                patchVersion,
                Collections.emptySet(),
                Collections.emptyList());
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validate(int majorVersion,
                          int minorVersion,
                          int patchVersion,
                          Set<Tag> tags) {
        int minimumVersionNumber = 0;

        if (majorVersion < minimumVersionNumber
                || minorVersion < minimumVersionNumber
                || patchVersion < minimumVersionNumber) {
            throw new IllegalArgumentException("The major, minor or patch version cannot be negative");
        } else if (tags == null) {
            throw new IllegalArgumentException("The addTag set cannot be null");
        } else if (tags.contains(null)) {
            throw new IllegalArgumentException("The addTag set cannot contain null elements");
        }
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public int getPatchVersion() {
        return this.patchVersion;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d%s%s",
                this.majorVersion,
                this.minorVersion,
                this.patchVersion,
                this.tags.isEmpty()
                        ? ""
                        : "-" + this.getJoinedTags(),
                this.metadata.isEmpty()
                        ? ""
                        : "+" + this.getJoinedMetadata());
    }

    private String getJoinedTags() {
        String prefix = ".";

        return this.tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(prefix));
    }

    private String getJoinedMetadata() {
        String prefix = ".";

        return String.join(prefix, this.metadata);
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.majorVersion;
        hash = 31 * hash + this.minorVersion;
        hash = 31 * hash + this.patchVersion;
        hash = 31 * hash + this.tags.hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof Version)) {
            return false;
        }

        Version other = (Version) object;

        return Objects.equals(other.majorVersion, this.majorVersion)
                && Objects.equals(other.minorVersion, this.minorVersion)
                && Objects.equals(other.patchVersion, this.patchVersion);
    }

    @Override
    public int compareTo(Version o) {
        int result = this.majorVersion - o.majorVersion;

        if (result == MIN_VERSION.majorVersion) {
            result = this.minorVersion - o.minorVersion;

            if (result == MIN_VERSION.minorVersion) {
                result = this.patchVersion - o.patchVersion;
            }
        }

        return result;
    }

    public static class Builder {
        private final Set<Tag> tags;
        private final List<String> metadata;
        private int majorVersion;
        private int minorVersion;
        private int patchVersion;

        Builder() {
            this.tags = new HashSet<>();
            this.metadata = new ArrayList<>();
        }

        public Builder major(int majorVersion) {
            this.majorVersion = majorVersion;

            return this;
        }

        public Builder minor(int minorVersion) {
            this.minorVersion = minorVersion;

            return this;
        }

        public Builder patch(int patchVersion) {
            this.patchVersion = patchVersion;

            return this;
        }

        public Builder addTag(Tag tag) {
            this.tags.add(tag);

            return this;
        }

        public Builder meta(String meta) {
            this.metadata.add(meta);

            return this;
        }

        public Version build() {
            return new Version(this.majorVersion,
                    this.minorVersion,
                    this.patchVersion,
                    this.tags,
                    this.metadata);
        }
    }
}