package com.github.foskel.douglas.util;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Foskel
 */
public final class ToStringBuilder {
    private static final String NULL_ATTRIBUTE = "null";

    private final Object source;
    private final boolean useIdentityToString;
    private final Deque<String> attributes;
    private final StringBuilder attributeBuilder;

    public ToStringBuilder(Object source) {
        this(source, false);
    }

    public ToStringBuilder(Object source, boolean useIdentityToString) {
        this.source = source;
        this.useIdentityToString = useIdentityToString;
        this.attributes = new ArrayDeque<>();
        this.attributeBuilder = new StringBuilder();
    }

    private static String identityToString(Object obj) {
        return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
    }

    public ToStringBuilder attribute(Object attribute) {
        this.attributeBuilder.setLength(0);
        this.attributes.add(this.attributeBuilder.append(attribute.getClass().getSimpleName())
                .append("=")
                .append(this.toString(attribute))
                .toString());

        return this;
    }

    public String build() {
        StringBuilder resultBuilder = new StringBuilder();
        String start = this.useIdentityToString ? identityToString(this.source) : this.source.getClass().getSimpleName();

        resultBuilder.append(start).append("{");

        while (!this.attributes.isEmpty()) {
            String lastAttribute = this.attributes.getLast();
            String attribute = this.attributes.poll();

            resultBuilder.append(attribute);

            if (!attribute.equals(lastAttribute)) {
                resultBuilder.append(",");
            }
        }

        resultBuilder.append("}");

        return resultBuilder.toString();
    }

    private String toString(Object obj) {
        if (obj == null) {
            return NULL_ATTRIBUTE;
        }

        return this.useIdentityToString
                ? identityToString(obj)
                : obj.toString();
    }
}
