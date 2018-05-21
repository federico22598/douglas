package com.github.foskel.douglas.module.locate;

import com.github.foskel.douglas.module.Module;

import java.util.Map;

public final class SynchronizedModuleLocatorProvider implements ModuleLocatorProvider {

    @Override
    public ModuleLocatorService create(Map<String, Module> modules) {
        return new SynchronizedModuleLocatorService(modules);
    }
}
