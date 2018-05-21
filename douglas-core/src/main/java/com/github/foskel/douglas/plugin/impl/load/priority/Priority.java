package com.github.foskel.douglas.plugin.impl.load.priority;

import com.github.foskel.douglas.plugin.load.priority.PluginPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Priority {
    PluginPriority load() default PluginPriority.NORMAL;

    PluginPriority unload() default PluginPriority.NORMAL;
}