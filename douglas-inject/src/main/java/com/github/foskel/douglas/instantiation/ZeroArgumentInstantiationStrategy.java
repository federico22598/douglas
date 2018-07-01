package com.github.foskel.douglas.instantiation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Foskel
 */
public final class ZeroArgumentInstantiationStrategy<T> implements InstantiationStrategy<T> {

    @Override
    public T instantiate(Class<? extends T> type, ClassLoader classLoader) throws InstantiationException {
        try {
            Constructor<? extends T> constructor = type.getDeclaredConstructor();

            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Unable to find a valid zero-argument constructor " +
                    "for class \"" + type.getCanonicalName() + "\"");
        } catch (IllegalAccessException e) {
            throw new InstantiationException("Cannot access zero-argument constructor " +
                    "of class \"" + type.getCanonicalName() + "\":", e);
        } catch (InvocationTargetException | java.lang.InstantiationException e) {
            throw new InstantiationException("An exception was thrown while trying to " +
                    "instantiate \"" + type.getCanonicalName() + "\":", e);
        }
    }
}
