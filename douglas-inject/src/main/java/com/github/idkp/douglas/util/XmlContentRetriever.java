package com.github.idkp.douglas.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Objects;
import java.util.Optional;

public final class XmlContentRetriever {
    private XmlContentRetriever() {
    }

    public static Optional<String> findFirstAsText(Document parent, String content) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(content, "content");

        return findFirst(parent, content).map(Node::getTextContent);
    }

    public static Optional<Node> findFirst(Document parent, String content) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(content, "content");

        int firstNodeIndex = 0;

        return find(parent, content, firstNodeIndex);
    }

    public static Optional<Node> find(Document parent, String content, int index) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(content, "content");

        NodeList nodes = parent.getElementsByTagName(content);
        Node node = nodes.item(index);

        if (node == null) {
            return Optional.empty();
        }

        String firstNodeName = node.getNodeName();

        if (!firstNodeName.equals(content)) {
            return Optional.empty();
        }

        return Optional.of(node);
    }

    public static Optional<String> findFirstAsText(Element parent, String content) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(content, "content");

        return findFirst(parent, content).map(Node::getTextContent);
    }

    public static Optional<Node> findFirst(Element parent, String content) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(content, "content");

        int firstNodeIndex = 0;

        return find(parent, content, firstNodeIndex);
    }

    public static Optional<Node> find(Element parent, String content, int index) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(content, "content");

        NodeList nodes = parent.getElementsByTagName(content);
        Node node = nodes.item(index);

        if (node == null) {
            return Optional.empty();
        }

        String firstNodeName = node.getNodeName();

        if (!firstNodeName.equals(content)) {
            return Optional.empty();
        }

        return Optional.of(node);
    }
}
