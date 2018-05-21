package com.github.foskel.douglas.module;

import com.github.foskel.camden.property.properties.StringProperty;
import com.github.foskel.camden.property.scan.annotation.Propertied;

@Propertied
public final class TestModule extends AbstractModule {
    private final StringProperty testStringProperty = new StringProperty("name", "value");

    @Override
    public void load() {
        super.load();

        System.out.println("Loading " + this.getClass().getSimpleName());
    }

    @Override
    public void unload() {
        super.unload();

        System.out.println("Unloading " + this.getClass().getSimpleName());
    }

    @Override
    public String getName() {
        return "Test";
    }

    public StringProperty getTestStringProperty() {
        return this.testStringProperty;
    }
}
