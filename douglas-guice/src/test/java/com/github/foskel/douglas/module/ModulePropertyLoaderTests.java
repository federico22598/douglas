package com.github.foskel.douglas.module;

import com.github.foskel.douglas.module.io.JsonModulePropertyLoader;
import com.github.foskel.douglas.module.io.ModulePropertyLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public final class ModulePropertyLoaderTests {
    private static final String PROPERTIES_RESOURCE_PATH = "test_module_properties.json";
    private static final String STRING_PROPERTY_NEW_VALUE = "newValue";

    private final ModulePropertyLoader propertyLoader = new JsonModulePropertyLoader();
    private final TestModule testModule = new TestModule();
    private String previousStringPropertyValue;

    @Before
    public void loadProperties() {
        this.testModule.load();

        this.previousStringPropertyValue = this.testModule.getTestStringProperty().getValue();

        try {
            this.propertyLoader.load(this.testModule, getPropertiesResourceStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InputStream getPropertiesResourceStream() {
        return ModulePropertyLoaderTests.class.getClassLoader().getResourceAsStream(PROPERTIES_RESOURCE_PATH);
    }

    @Test
    public void testModule_stringProperty_hasValueBeenUpdated() {
        String stringPropertyValue = this.testModule.getTestStringProperty().getValue();

        assertNotEquals(stringPropertyValue, this.previousStringPropertyValue);
        assertEquals(stringPropertyValue, STRING_PROPERTY_NEW_VALUE);
    }
}
