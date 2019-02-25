package com.github.foskel.douglas.annotations;

import com.github.foskel.douglas.plugin.load.PluginPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Priority {
    PluginPriority value();
}
