package com.github.foskel.douglas.module.locate;

import com.github.foskel.douglas.module.Module;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Fred
 * @since 4/9/2017
 */
public interface ModuleLocatorService {
    Optional<Module> findModule(String identifier);

    Optional<Module> findModule(Class<? extends Module> moduleClass);

    Set<Module> findModules(Predicate<Module> condition);
}