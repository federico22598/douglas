package com.github.foskel.douglas.module.attribute;

import com.github.foskel.douglas.module.Module;

import java.util.*;
import java.util.stream.Collectors;

public enum AnnotationAttributeManager implements AttributeManager {
    INSTANCE;

    private final Map<Class<? extends Module>, Object> attributes = new HashMap<>();

    @Override
    public boolean scanAndRegister(Class<? extends Module> moduleType) {
        if (!moduleType.isAnnotationPresent(Attribute.class)) {
            return false;
        }

        Attribute categoryAnnotation = moduleType.getAnnotation(Attribute.class);

        return this.register(moduleType, categoryAnnotation.value());
    }

    @Override
    public boolean register(Class<? extends Module> moduleType, Object attribute) {
        if (!this.attributes.containsKey(moduleType)) {
            this.attributes.put(moduleType, attribute);

            return true;
        }

        return false;
    }

    @Override
    public boolean unregister(Class<? extends Module> moduleType) {
        if (this.attributes.containsKey(moduleType)) {
            this.attributes.remove(moduleType);

            return true;
        }

        return false;
    }

    @Override
    public boolean has(Class<? extends Module> moduleType) {
        return this.find(moduleType).isPresent();
    }

    @Override
    public List<Class<? extends Module>> find(Object attribute) {
        return this.attributes.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == attribute)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Object> find(Class<? extends Module> moduleType) {
        return Optional.ofNullable(this.attributes.get(moduleType));
    }

    @Override
    public <T> Optional<T> find(Class<? extends Module> moduleType, Class<? extends T> attributeType) {
        Object result = this.attributes.get(moduleType);

        if (result == null || result.getClass().isInstance(attributeType)) {
            return Optional.empty();
        }

        return Optional.of((T) result);
    }

    @Override
    public Map<Class<? extends Module>, Object> findAllAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }
}