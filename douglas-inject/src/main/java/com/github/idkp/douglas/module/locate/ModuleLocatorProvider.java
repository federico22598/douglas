package com.github.idkp.douglas.module.locate;

import com.github.idkp.douglas.module.Module;

import java.util.Map;

public interface ModuleLocatorProvider {
    ModuleLocatorService create(Map<String, Module> modules);
}
