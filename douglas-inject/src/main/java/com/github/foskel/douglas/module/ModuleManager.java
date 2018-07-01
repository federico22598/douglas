package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.locate.ModuleLocatorService;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * @author Foskel
 * @since 3/24/2017
 */
public interface ModuleManager {
    void load(Path directory);

    void unload(Path directory);

    boolean register(Module module);

    boolean unregister(String moduleName);

    ModuleLocatorService getLocator();

    Collection<Module> findAllModules();

    void clear();
}