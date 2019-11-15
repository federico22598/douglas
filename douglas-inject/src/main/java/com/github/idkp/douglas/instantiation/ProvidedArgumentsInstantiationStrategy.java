package com.github.idkp.douglas.instantiation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public final class ProvidedArgumentsInstantiationStrategy<T> implements InstantiationStrategy<T> {
    private final Object[] arguments;
    private final Object[] argumentTypes;

    public ProvidedArgumentsInstantiationStrategy(Object[] arguments, Object[] argumentTypes) {
        this.arguments = arguments;
        this.argumentTypes = argumentTypes;
    }

    private static Constructor<?> findConstructor(Class<?> type, Object[] argumentTypes) throws NoSuchMethodException {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (Arrays.equals(constructor.getParameterTypes(), argumentTypes)) {
                return constructor;
            }
        }

        throw new NoSuchMethodException("No constructor(s) matching parameters " + Arrays.toString(argumentTypes));
    }

    @Override
    @SuppressWarnings({"JavaReflectionMemberAccess", "unchecked"})
    public T instantiate(Class<? extends T> type, ClassLoader classLoader) throws InstantiationException {
        Constructor<?> constructor;

        try {
            constructor = findConstructor(type, this.argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Unable to instantiate plugin " +
                    "with type \"" + type.getSimpleName() + "\":", e);
        }

        this.ensureAccessibility(constructor);

        try {
            return (T) constructor.newInstance(this.arguments);
        } catch (java.lang.InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchElementException e) {
            throw new InstantiationException("Unable to instantiate plugin " +
                    "with type \"" + type.getCanonicalName() + "\":", e);
        }
    }

    private void ensureAccessibility(Constructor<?> constructor) {
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
    }
}