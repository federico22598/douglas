package com.github.foskel.douglas.module;

import com.github.foskel.camden.property.PropertyManager;
import com.github.foskel.douglas.core.traits.Loadable;
import com.github.foskel.douglas.core.traits.Reloadable;
import com.github.foskel.haptor.StandardDependencySystem;

import java.nio.file.Path;

/**
 * TODO: Stop making each Module have its own PropertyManager
 *
 * @author Fred
 * @since 4/4/2017
 */

public interface Module extends Loadable, Reloadable {

    String getName();

    @Override
    void load();

    @Override
    void unload();

    @Override
    default void reload() {
        this.unload();
        this.load();
    }

    Path getDataFile();

    void setDataFile(Path dataFile);
}