package com.github.foskel.douglas.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Supply {
    String groupId();

    String artifactId();

    String version() default "<latest>";

    String name();
}