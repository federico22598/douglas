package com.github.foskel.douglas.module.locate;

import com.github.foskel.douglas.module.Module;

import java.util.Map;

public interface ModuleLocatorProvider {
    ModuleLocatorService create(Map<String, Module> modules);
}
