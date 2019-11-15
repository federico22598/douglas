package com.github.idkp.douglas.module;

import com.github.idkp.douglas.module.locate.ModuleLocatorService;

import java.util.Collection;

/**
 * 3/24/2017
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