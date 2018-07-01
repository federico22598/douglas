package com.github.foskel.douglas.annotations;

import com.github.foskel.douglas.plugin.load.priority.PluginPriority;

import java.lang.annotation.*;

/**
 * This annotation is used to determine a plugin's loading or unloading
 * priority. It should be declared on a plugin main class' type. For example:
 *
 * <code>
 *     @Priority({load = PluginPriority.FIRST, unload = PluginPriority.LAST})
 *     public class MyPlugin implements Plugin {
 *     ... methods ...
 *     }
 * </code>
 *
 * Here, MyPlugin will be the first plugin to load, and the last to unload.
 *
 * @author Foskel
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Priority {
    PluginPriority load() default PluginPriority.NORMAL;

    PluginPriority unload() default PluginPriority.NORMAL;
}