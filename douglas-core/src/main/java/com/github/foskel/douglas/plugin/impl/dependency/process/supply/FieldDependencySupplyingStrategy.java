package com.github.foskel.douglas.plugin.impl.dependency.process.supply;

import com.github.foskel.douglas.annotations.Supply;
import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.core.version.Versions;
import com.github.foskel.douglas.core.version.parse.VersionParsingException;
import com.github.foskel.douglas.plugin.Plugin;
import com.github.foskel.douglas.plugin.dependency.supply.DependencySupplyingException;
import com.github.foskel.douglas.plugin.dependency.supply.DependencySupplyingStrategy;
import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
import com.github.foskel.douglas.plugin.manifest.PluginDescriptor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Foskel
 */
public final class FieldDependencySupplyingStrategy implements DependencySupplyingStrategy {
    private final PluginLocatorService dependencyLocator;

    FieldDependencySupplyingStrategy(PluginLocatorService dependencyLocator) {
        this.dependencyLocator = dependencyLocator;
    }

    private static PluginDescriptor createDescriptor(Supply supplyAnnotation) {
        return PluginDescriptor.builder()
                .groupId(supplyAnnotation.groupId())
                .artifactId(supplyAnnotation.artifactId())
                .version(parseVersion(supplyAnnotation.version()))
                .name(supplyAnnotation.name())
                .build();
    }

    private static Version parseVersion(String versionAsString) {
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
                .filter(field -> shouldReplace(field, source))
                .forEach(field -> {
                    Supply supplyAnnotation = field.getAnnotation(Supply.class);
                    PluginDescriptor descriptor = createDescriptor(supplyAnnotation);
                    Plugin dependency = this.dependencyLocator.find(descriptor).orElseThrow(NoSuchElementException::new);

                    setValue(field, source, dependency);
                });
    }

    private boolean shouldReplace(Field field, Object source) {
        Supply supplyAnnotation = field.getAnnotation(Supply.class);

        if (supplyAnnotation == null) {
            return false;
        }

        PluginDescriptor descriptor = createDescriptor(supplyAnnotation);
        Optional<Plugin> dependencyResult = this.dependencyLocator.find(descriptor);

        if (!dependencyResult.isPresent()) {
            throw new DependencySupplyingException("Unable to supply plugin dependency instance " +
                    "of field \"" + field.getName() + "\" annotated with " + Supply.class.getCanonicalName() +
                    " from class \"" + source.getClass().getCanonicalName() + "\"");
        }

        return getValue(field, source) == null;
    }
}
