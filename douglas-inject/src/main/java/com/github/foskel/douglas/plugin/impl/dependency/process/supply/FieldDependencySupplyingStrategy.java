package com.github.foskel.douglas.plugin.impl.dependency.process.supply;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.core.version.Versions;
import com.github.foskel.douglas.core.version.parse.VersionParsingException;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.supply.DependencySupplyingException;
import com.github.foskel.douglas.plugin.dependency.supply.DependencySupplyingStrategy;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.impl.descriptor.PluginDescriptorBuilder;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public final class FieldDependencySupplyingStrategy implements DependencySupplyingStrategy {
    private final PluginLocatorService dependencyLocator;

    FieldDependencySupplyingStrategy(PluginLocatorService dependencyLocator) {
        this.dependencyLocator = dependencyLocator;
    }

    @Override
    public void scanAndSupply(Object source) throws DependencySupplyingException {
        Class<?> sourceType = source.getClass();
        Field[] rawDeclaredFields = sourceType.getDeclaredFields();
        List<Field> declaredFields = Arrays.asList(rawDeclaredFields);

        if (declaredFields.isEmpty()) {
            return;
        }

        declaredFields.stream()
                .peek(FieldDependencySupplyingStrategy::ensureAccessibility)
                .filter(field -> this.shouldReplace(field, source))
                .forEach(field -> {
                    Supply supplyAnnotation = field.getAnnotation(Supply.class);

                    PluginDescriptor information = this.getInformationFrom(supplyAnnotation);
                    Plugin dependency = this.dependencyLocator.find(information)
                            .orElseThrow(NoSuchElementException::new);

                    setValue(field, source, dependency);
                });
    }

    private PluginDescriptor getInformationFrom(Supply supplyAnnotation) {
        return new PluginDescriptorBuilder()
                .withGroupId(supplyAnnotation.groupId())
                .withArtifactId(supplyAnnotation.artifactId())
                .withVersion(extractVersionFrom(supplyAnnotation.version()))
                .withName(supplyAnnotation.name())

                .build();
    }

    private static Version extractVersionFrom(String versionAsString) {
        try {
            return Versions.of(versionAsString);
        } catch (VersionParsingException e) {
            e.printStackTrace();

            return null;
        }
    }

    private static void ensureAccessibility(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    private boolean shouldReplace(Field field,
                                  Object source) {
        if (!field.isAnnotationPresent(Supply.class)) {
            return false;
        }

        Supply supplyAnnotation = field.getAnnotation(Supply.class);

        PluginDescriptor information = this.getInformationFrom(supplyAnnotation);
        Optional<Plugin> dependencyResult = this.dependencyLocator.find(information);

        if (!dependencyResult.isPresent()) {
            throw new DependencySupplyingException("Unable to supply plugin dependency instance " +
                    "of field \"" + field.getName() + "\" annotated with " + Supply.class.getCanonicalName() +
                    " from class \"" + source.getClass().getCanonicalName() + "\"");
        }

        return getValue(field, source) == null;
    }

    private static void setValue(Field field, Object owner, Object value) {
        try {
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Object getValue(Field field, Object owner) {
        try {
            return field.get(owner);
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            return null;
        }
    }
}
