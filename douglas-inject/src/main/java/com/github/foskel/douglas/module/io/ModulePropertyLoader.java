package com.github.foskel.douglas.module.io;

import com.github.foskel.douglas.module.Module;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;

public interface ModulePropertyLoader {
    void load(Collection<Module> modules, Path directory) throws IOException;

    void load(Module module, InputStream resourceStream) throws IOException;
}
