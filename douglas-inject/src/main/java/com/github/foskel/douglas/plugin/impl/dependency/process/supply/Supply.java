package com.github.foskel.douglas.plugin.impl.dependency.process.supply;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Supply {
    String groupId();

    String artifactId();

    String version() default "";

    String name();
}