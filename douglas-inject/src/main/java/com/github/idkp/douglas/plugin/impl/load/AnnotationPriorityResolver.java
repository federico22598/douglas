package com.github.idkp.douglas.plugin.impl.load;

import com.github.idkp.douglas.annotations.Priority;
import com.github.idkp.douglas.plugin.Plugin;
import com.github.idkp.douglas.plugin.load.PluginPriority;
import com.github.idkp.douglas.plugin.load.PluginPriorityResolver;

import java.lang.reflect.Method;

public final class AnnotationPriorityResolver implements PluginPriorityResolver {
    private static final PluginPriority DEFAULT_PRIORITY = PluginPriority.NORMAL;

    @Override
    public PluginPriority resolveLoading(Plugin plugin) {
        return extract(plugin.getClass(), "load");
    }

    @Override
    public PluginPriority resolveUnloading(Plugin plugin) {
        return extract(plugin.getClass(), "unload");
    }

    private static PluginPriority extract(Class<?> type, String methodName) {
        Method targetMethod;

        try {
            targetMethod = type.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return DEFAULT_PRIORITY;
        }

        Priority priorityAnno = targetMethod.getAnnotation(Priority.class);

        if (priorityAnno == null) {
            return DEFAULT_PRIORITY;
        }

        return priorityAnno.value();
    }
}
