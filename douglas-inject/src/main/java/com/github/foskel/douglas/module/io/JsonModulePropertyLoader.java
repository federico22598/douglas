package com.github.foskel.douglas.module.io;

import com.github.foskel.camden.property.Property;
import com.github.foskel.camden.property.PropertyManager;
import com.github.foskel.camden.property.locate.PropertyLocatorService;
import com.github.foskel.douglas.module.Module;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by Fred on 6/6/2017.
 */
public final class JsonModulePropertyLoader implements ModulePropertyLoader {

    @Override
    public void load(Collection<Module> modules, Path directory) {
        if (modules.isEmpty()) {
            return;
        }

        for (Module module : modules) {
            IO ioAnnotation = module.getClass().getAnnotation(IO.class);
            Path source = (ioAnnotation != null && ioAnnotation.customDir())
                    ? directory.resolve(module.getName().toLowerCase()).resolve(module.getName() + ".json")
                    : directory.resolve(module.getName() + ".json");

            module.getPropertyManager().register(module);

            if (!this.shouldSkip(module, directory, source)) {
                PropertyManager propertyManager = module.getPropertyManager();
                Collection<Property<?>> properties = propertyManager.findAllProperties();

                if (!properties.isEmpty()) {
                    JsonModulePropertyLoadWorker.load(module, source);
                }
            }

            module.setDataFile(source);
            module.load();
        }
    }

    @Override
    public void load(Module module, InputStream resourceStream) {
        PropertyManager propertyManager = module.getPropertyManager();
        Collection<Property<?>> properties = propertyManager.findAllProperties();

        if (!properties.isEmpty()) {
            JsonModulePropertyLoadWorker.load(module, resourceStream);
        }
    }

    private boolean shouldSkip(Module module, Path directory, Path source) {
        boolean directoryNotSuitable = this.isDirectoryNotSuitableForLoadingProperties(directory);
        boolean sourceNotSuitable = this.isPropertyContainingFileNotSuitableForLoadingProperties(source);
        IO ioAnnotation = module.getClass().getAnnotation(IO.class);
        boolean forceCreation = ioAnnotation != null && ioAnnotation.forceCreation();

        return !forceCreation && (directoryNotSuitable || sourceNotSuitable);
    }

    private boolean isDirectoryNotSuitableForLoadingProperties(Path directory) {
        return directory == null
                || Files.notExists(directory)
                || !Files.isDirectory(directory);
    }

    private boolean isPropertyContainingFileNotSuitableForLoadingProperties(Path source) {
        return source == null
                || Files.notExists(source)
                || Files.isDirectory(source)
                || !source.getFileName().toString().endsWith(".json");
    }

    private static class JsonModulePropertyLoadWorker {
        public static void load(Module module, InputStream source) {
            Optional<JsonElement> elementResult = getJsonElement(source);

            if (!elementResult.isPresent()) {
                return;
            }

            JsonElement jsonElement = elementResult.get();

            load(module, jsonElement);
        }

        public static void load(Module module, Path source) {
            Optional<JsonElement> elementResult = getJsonElement(source);

            if (!elementResult.isPresent()) {
                return;
            }

            JsonElement jsonElement = elementResult.get();

            load(module, jsonElement);
        }

        public static void load(Module module, JsonElement jsonElement) {
            if (!jsonElement.isJsonObject()) {
                return;
            }

            JsonObject root = jsonElement.getAsJsonObject();
            Iterator<Map.Entry<String, JsonElement>> entryIterator = root.entrySet().iterator();

            while (entryIterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = entryIterator.next();
                String propertyIdentifier = entry.getKey();
                PropertyLocatorService propertyLocator = module.getPropertyManager().getPropertyLocator();
                Optional<Property<?>> propertyResult = propertyLocator.findProperty(module, propertyIdentifier);

                if (!propertyResult.isPresent()) {
                    entryIterator.remove();

                    return;
                }

                Property property = propertyResult.get();

                property.setValueParsingInput(entry.getValue().getAsString());
            }
        }

        //TODO: Replace with Gson deserializer?
        private static Optional<JsonElement> getJsonElement(Path source) {
            Objects.requireNonNull(source, "source");

            if (Files.notExists(source) || Files.isDirectory(source)) {
                return Optional.empty();
            }

            try (Reader reader = new FileReader(source.toFile())) {
                JsonElement root = new JsonParser().parse(reader);

                return Optional.of(root);
            } catch (IOException e) {
                e.printStackTrace();

                return Optional.empty();
            }
        }

        private static Optional<JsonElement> getJsonElement(InputStream source) {
            Objects.requireNonNull(source, "source");

            try (Reader reader = new InputStreamReader(source)) {
                JsonElement root = new JsonParser().parse(reader);

                return Optional.of(root);
            } catch (IOException e) {
                e.printStackTrace();

                return Optional.empty();
            }
        }
    }
}