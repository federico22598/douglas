package com.github.foskel.douglas.core.category;

import com.github.foskel.douglas.core.traits.Named;

public class Category implements Named {
    private final int index;
    private final String name;

    public Category(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public Category(String name) {
        this(name, -1);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }
}
