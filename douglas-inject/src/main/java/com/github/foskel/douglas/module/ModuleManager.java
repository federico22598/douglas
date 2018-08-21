package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.locate.ModuleLocatorService;

import java.util.Collection;

/**
 * @author Foskel
 * @since 3/24/2017
 */
public interface ModuleManager {
    void load();

    void unload();

    boolean register(Module module);

    boolean unregister(String moduleName);

    ModuleLocatorService getLocator();

    Collection<Module> findAllModules();

    void clear();
}