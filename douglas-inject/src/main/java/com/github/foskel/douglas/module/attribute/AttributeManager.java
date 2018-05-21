package com.github.foskel.douglas.module.attribute;

import com.github.foskel.douglas.module.Module;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AttributeManager {
    boolean scanAndRegister(Class<? extends Module> moduleType);

    boolean register(Class<? extends Module> moduleType, Object attribute);

    boolean unregister(Class<? extends Module> moduleType);

    boolean has(Class<? extends Module> moduleType);

    List<Class<? extends Module>> find(Object attribute);

    Optional<Object> find(Class<? extends Module> moduleType);

    <T> Optional<T> find(Class<? extends Module> moduleType, Class<? extends T> attributeType);

    Map<Class<? extends Module>, Object> findAllAttributes();
}