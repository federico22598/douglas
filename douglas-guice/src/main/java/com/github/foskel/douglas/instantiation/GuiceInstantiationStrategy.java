package com.github.foskel.douglas.instantiation;

import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.Collection;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class is used to instantiate a class using a Guice injector
 *
 * @param <T> the type of the class that will be instantiated
 * @author Foskel
 * @see com.google.inject.Injector#getInstance(Class)
 */
public final class GuiceInstantiationStrategy<T> implements InstantiationStrategy<T> {
    private final Injector parentInjector;
    private final Set<Class<? extends Module>> moduleCache = new HashSet<>();

    public GuiceInstantiationStrategy(Injector parentInjector) {
        this.parentInjector = parentInjector;
    }

    private static <T> Stream<T> stream(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            Collection<T> collection = (Collection<T>) iterable;

            return collection.stream();
        }

        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public T instantiate(Class<? extends T> type, ClassLoader classLoader) throws InstantiationException {
        Injector childInjector = this.getChildInjector(classLoader);

        try {
            return childInjector.getInstance(type);
        } catch (Throwable throwable) {
            throw new InstantiationException("Unable to instantiate " + type.getCanonicalName() + ":", throwable);
        }
    }

    private Injector getChildInjector(ClassLoader classLoader) {
        ServiceLoader<Module> moduleServiceLoader = ServiceLoader.load(
                Module.class, classLoader);

        if (!moduleServiceLoader.iterator().hasNext()) {
            return this.parentInjector;
        }

        Stream<Module> serviceLoaderModules = stream(moduleServiceLoader);
        Set<Class<? extends Module>> serviceLoaderModuleTypes = serviceLoaderModules
                .map(Module::getClass)
                .collect(Collectors.toSet());

        if (!this.moduleCache.addAll(serviceLoaderModuleTypes)) {
            return this.parentInjector;
        }

        return this.parentInjector.createChildInjector(moduleServiceLoader);
    }
}