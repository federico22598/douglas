package com.github.foskel.douglas.plugin.impl.resource;

import com.github.foskel.douglas.annotations.Resource;
import com.github.foskel.douglas.plugin.resource.ResourceHandler;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

/**
 * @author Foskel
 */
public final class AnnotationResourceHandler implements ResourceHandler {

    private static Object getResource(Class<?> type, ClassLoader classLoader, Field field) throws InvocationTargetException, IllegalAccessException {
        Resource annotation = field.getAnnotation(Resource.class);

        if (annotation == null) {
            return null;
        }

        String resourcePath = annotation.value();
        String converterName = annotation.converter();
        Class<?> fieldType = field.getType();

        if (converterName.isEmpty()) {
            if (fieldType == URL.class) {
                return getResourceAsURL(resourcePath, classLoader);
            } else if (fieldType == InputStream.class) {
                return getResourceAsStream(resourcePath, classLoader);
            } else {
                throw new IllegalStateException("Resource field type must be of type java.net.URL or java.io.InputStream. Otherwise, a converter must be specified");
            }
        } else {
            Method converter = getConverter(type, fieldType, converterName);

            if (converter == null) {
                throw new IllegalStateException("Resource converter method \"" + converterName + "\" not found");
            }

            Class<?> resourceType = converter.getParameterTypes()[0];
            Object resource;

            if (resourceType == URL.class) {
                resource = getResourceAsURL(resourcePath, classLoader);
            } else if (resourceType == InputStream.class) {
                resource = getResourceAsStream(resourcePath, classLoader);
            } else {
                throw new IllegalStateException("Resource converter parameter must be of type java.net.URL or java.io.InputStream");
            }

            if (!converter.isAccessible()) {
                converter.setAccessible(true);
            }

            return converter.invoke(null, resource);
        }
    }

    private static Method getConverter(Class<?> owner, Class<?> fieldType, String name) {
        for (Method method : owner.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers()) || !method.getName().equals(name) || method.getReturnType() != fieldType) {
                continue;
            }

            if (method.getParameterTypes().length != 1) {
                throw new IllegalStateException("Resource converter method must only have one parameter");
            }

            return method;
        }

        return null;
    }

    private static InputStream getResourceAsStream(String path, ClassLoader classLoader) {
        return classLoader.getResourceAsStream(path);
    }

    private static URL getResourceAsURL(String path, ClassLoader classLoader) {
        return classLoader.getResource(path);
    }

    @Override
    public void handle(Class<?> type, ClassLoader classLoader) {
        for (Field field : type.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            try {
                Object resource = getResource(type, classLoader, field);

                if (resource != null) {
                    field.set(null, resource);
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
