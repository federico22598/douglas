package com.github.foskel.douglas.module.dependency;

import com.github.foskel.douglas.module.ModuleManager;

/**
 * @author Foskel
 */
public interface ModuleDependencySatisfyingService {
    void satisfy(ModuleManager moduleManager);
}
