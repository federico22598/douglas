package com.github.idkp.douglas.module;

public final class TestModule extends AbstractModule {
    TestModule() {
        super("Test");
    }

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
}