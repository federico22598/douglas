package com.github.foskel.douglas.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractList;

/**
 * @author Foskel
 */
public final class JavaListNodeListAdapter extends AbstractList<Node> {
    private final NodeList backingNodeList;

    public JavaListNodeListAdapter(NodeList backingNodeList) {
        this.backingNodeList = backingNodeList;
    }

    @Override
    public Node get(int index) {
        return this.backingNodeList.item(index);
    }

    @Override
    public int size() {
        return this.backingNodeList.getLength();
    }
}