package com.github.foskel.douglas.module;

import com.github.foskel.camden.property.PropertyManager;
import com.github.foskel.camden.property.StandardPropertyManager;
import com.github.foskel.douglas.module.attribute.AnnotationAttributeManager;
import com.github.foskel.douglas.module.attribute.AttributeManager;
import com.github.foskel.haptor.impl.ClassDependencySystem;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Abstract implementation of {@link Module}
 *
 * @author Fred
 * @since 2/21/2017
 */

@SuppressWarnings("WeakerAccess")
public abstract class AbstractModule implements Module {
    protected final ClassDependencySystem<Module> dependencySystem = new ClassDependencySystem<>();
    protected final PropertyManager propertyManager = new StandardPropertyManager();
    private Path dataFile;

    private static String identityToString(Object o) {
        return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
    }

    @Override
    public void load() {
        this.dependencySystem.registerDependencies(this);
        this.registerAttributes();
    }

    private void registerAttributes() {
        AnnotationAttributeManager.INSTANCE.scanAndRegister(this.getClass());
    }

    @Override
    public void unload() {
        this.dependencySystem.unregisterDependencies(this);
        this.propertyManager.unregister(this);
        this.unregisterAttributes();
    }

    private void unregisterAttributes() {
        AttributeManager attributeManager = AnnotationAttributeManager.INSTANCE;

        attributeManager.unregister(this.getClass());
    }

    @Override
    public Path getDataFile() {
        return this.dataFile;
    }

    @Override
    public void setDataFile(Path dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public ClassDependencySystem<Module> getDependencySystem() {
        return this.dependencySystem;
    }

    @Override
    public String toString() {
        return identityToString(this) + "{" +
                (this.getName() == null
                        ? "<null>"
                        : identityToString(this.getName()) + "[" + this.getName() + "]") + "," +
                (this.propertyManager.findAllProperties() == null
                        ? "<null>"
                        : identityToString(this.propertyManager.findAllProperties()) + "[" + this.propertyManager.findAllProperties() + "]")
                + "}";
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31 * hash + this.getName().hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof Module)) {
            return false;
        }

        Module other = (Module) object;

        return Objects.equals(other.getName(), this.getName());
    }
}