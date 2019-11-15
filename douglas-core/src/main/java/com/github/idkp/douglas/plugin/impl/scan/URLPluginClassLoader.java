package com.github.idkp.douglas.plugin.impl.scan;

import java.net.URL;
import java.net.URLClassLoader;

public final class URLPluginClassLoader extends URLClassLoader {
    URLPluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    URLPluginClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    public boolean isLoaded(String canonicalClassName) {
        return this.findLoadedClass(canonicalClassName) != null;
    }
}