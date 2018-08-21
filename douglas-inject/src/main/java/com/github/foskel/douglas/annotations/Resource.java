package com.github.foskel.douglas.annotations;

import java.lang.annotation.*;

/**
 * The {@code @Resource} annotation is used to declare a resource on a plugin's main class field.
 * The resource will be wrapped to an URL or an InputStream. It is the equivalent
 * to {@code <type>.class.getResource(<resourcePath>)} (URL) and
 * {@code <type>.class.getResourceAsStream(<resourcePath>)} (InputStream).
 * However, you can't use these methods inside a plugin, since it's class loader
 * is mixed with the other plugins.
 * <p>
 * You can use the annotation by either specifying directly a resource path, or
 * by using a wrapper method that only takes an URL or an InputStream as parameter.
 * For example:
 *
 * <code>
 * public class MyPlugin implements Plugin {
 *
 * @author Foskel
 * @Resource("test.txt") private static InputStream resourceAsInputStream;
 * @Resource("test.txt") private static URL resourceAsURL;
 * @Resource(value = "test.txt", converter = "wrapURL")
 * private URLContainer mappedURLResource;
 * @Resource(value = "test.txt", converter = "wrapInputStream")
 * private InputStreamContainer mappedInputStreamResource;
 * <p>
 * private static URLContainer wrapURL(URL url) {
 * return () -> url;
 * }
 * <p>
 * private static InputStreamContainer wrapInputStream(InputStream inputStream) {
 * return () -> inputStream;
 * }
 * @FunctionalInterface private interface URLContainer {
 * URL getUrl();
 * }
 * @FunctionalInterface private interface InputStreamContainer {
 * InputStream getInputStream();
 * }
 * }
 * </code>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Resource {
    String value();

    String converter() default "";
}