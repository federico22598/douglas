package myorganization.myplugin;

import com.github.foskel.douglas.plugin.AbstractPlugin;
import com.github.foskel.douglas.plugin.impl.resource.Resource;

import java.io.InputStream;
import java.net.URL;

public final class MyPlugin extends AbstractPlugin {

    @Resource("test.txt")
    private static InputStream resourceAsInputStream;

    @Resource("test.txt")
    private static URL resourceAsURL;

    @Resource(value = "test.txt", converter = "wrapURL")
    private URLContainer resourceWrappedToURLContainer;

    @Resource(value = "test.txt", converter = "wrapInputStream")
    private InputStreamContainer resourceWrappedToInputStreamContainer;

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    private static URLContainer wrapURL(URL url) {
        return () -> url;
    }

    private static InputStreamContainer wrapInputStream(InputStream inputStream) {
        return () -> inputStream;
    }

    @FunctionalInterface
    private interface URLContainer {
        URL getUrl();
    }

    @FunctionalInterface
    private interface InputStreamContainer {
        InputStream getInputStream();
    }
}