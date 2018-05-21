package com.github.foskel.douglas.module.attribute;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Fred
 * @since 4/25/2017
 */
public enum ModuleCategory {
    COMBAT("Combat"),
    MISCELLANEOUS("Miscellaneous"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    PLUGINS("Plugins"),
    RENDER("Render"),
    WORLD("World");

    private final String name;

    ModuleCategory(String name) {
        this.name = name;
    }

    public static Optional<ModuleCategory> of(String name) {
        return Arrays.stream(values())
                .filter(moduleCategory -> moduleCategory.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public static ModuleCategory of(int index) {
        return Arrays.stream(values())
                .filter(moduleCategory -> moduleCategory.ordinal() == index)
                .findFirst()
                .orElse(values()[values().length - 1]);
    }

    public String getName() {
        return this.name;
    }
}
