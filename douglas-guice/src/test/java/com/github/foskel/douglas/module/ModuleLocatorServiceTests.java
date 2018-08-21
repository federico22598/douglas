package com.github.foskel.douglas.module;

import com.github.foskel.douglas.guice.DouglasModulesModule;
import com.github.foskel.douglas.module.locate.ModuleLocatorProvider;
import com.github.foskel.douglas.module.locate.ModuleLocatorService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;

public final class ModuleLocatorServiceTests {
    private final TestModule testModule = new TestModule();
    private ModuleLocatorService moduleLocator;

    private static Injector createGuiceInjector() {
        return Guice.createInjector(new DouglasModulesModule());
    }

    @Before
    public void createModuleLocator() {
        Injector injector = createGuiceInjector();
        ModuleLocatorProvider locatorProvider = injector.getInstance(ModuleLocatorProvider.class);

        this.moduleLocator = locatorProvider.create(
                Collections.singletonMap(this.testModule.getName(), this.testModule));
    }

    @Test
    public void findModuleByName_resultEqualsTestModule() {
        Optional<Module> result = this.moduleLocator.findModule(this.testModule.getName());

        assertTrue(result.isPresent());
    }


    @Test
    public void findModuleByClass_resultEqualsTestModule() {
        Optional<Module> result = this.moduleLocator.findModule(TestModule.class);

        assertTrue(result.isPresent());
    }


    @Test
    public void findModulesByFilter_resultEqualsTestModule() {
        Set<Module> result = this.moduleLocator.findModules(module -> module.getName().startsWith(this.testModule.getName().substring(0, 3)));

        assertTrue(result.contains(this.testModule));
    }
}