package com.github.idkp.douglas.annotations;

import com.github.idkp.douglas.plugin.load.PluginPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Priority {
    PluginPriority value();
}
