package com.github.foskel.douglas.module.io;

import com.github.foskel.camden.property.Property;
import com.github.foskel.camden.property.PropertyManager;
import com.github.foskel.douglas.module.Module;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Fred
 * @since 6/6/2017
 */
public final class JsonModulePropertySaver implements ModulePropertySaver {

    @Override
    public void save(Collection<Module> modules, Path directory) throws IOException {
        if (modules.isEmpty()) {
            return;
        }

        for (Module module : modules) {
            PropertyManager propertyManager = module.getPropertyManager();
            Collection<Property<?>> properties = propertyManager.findAllProperties();
            Class<? extends Module> moduleType = module.getClass();
            IO ioAnnotation = moduleType.getAnnotation(IO.class);

            if (properties.isEmpty() && (ioAnnotation == null || !ioAnnotation.forceCreation())) {
                continue;
            }

            Path destination = (ioAnnotation != null && ioAnnotation.customDir())
                    ? directory.resolve(module.getName().toLowerCase()).resolve(module.getName() + ".json")
                    : directory.resolve(module.getName() + ".json");

            this.ensureDirIsSuitableForPropertyFile(directory);
            this.createPropertyFile(destination);

            if (!properties.isEmpty()) {
                JsonModulePropertySaveWorker.save(destination, module);
            }
        }
    }

    private void ensureDirIsSuitableForPropertyFile(Path directory) throws IOException {
        Objects.requireNonNull(directory, "directory");

        if (this.shouldDirectoryBeDeleted(directory)) {
            Files.delete(directory);
        }

        Files.createDirectories(directory);
    }

    private boolean shouldDirectoryBeDeleted(Path directory) {
        Objects.requireNonNull(directory, "directory");

        return Files.exists(directory) && !Files.isDirectory(directory);
    }

    private void createPropertyFile(Path destination) throws IOException {
        Objects.requireNonNull(destination, "destination");

        if (this.shouldPropertyFileBeDeleted(destination)) {
            if (Files.isDirectory(destination)) {
                Files.list(destination).forEach(subFile -> {
                    try {
                        Files.delete(subFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            Files.delete(destination);
        }

        Files.createFile(destination);
    }

    private boolean shouldPropertyFileBeDeleted(Path destination) {
        Objects.requireNonNull(destination, "destination");

        return Files.exists(destination)
                && (Files.isDirectory(destination) || !destination.endsWith(".json"));
    }

    private static class JsonModulePropertySaveWorker {
        private static final Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        static void save(Path destination, Module module) throws IOException {
            JsonObject root = new JsonObject();

            PropertyManager propertyManager = module.getPropertyManager();
            Collection<Property<?>> properties = propertyManager.findAllProperties();

            properties.forEach(property -> root.addProperty(property.getName(), property.getStringValue()));

            Files.write(destination, GSON.toJson(root).getBytes("UTF-8"));
        }
    }
}