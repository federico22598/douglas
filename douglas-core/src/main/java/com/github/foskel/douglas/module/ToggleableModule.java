package com.github.foskel.douglas.module;

import com.github.foskel.douglas.core.traits.Toggleable;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * @author Fred
 * @since 2/2/2017
 */

public abstract class ToggleableModule extends AbstractModule implements Toggleable {
    private final Queue<Consumer<ToggleableModule>> enableListeners = new ArrayDeque<>();
    private final Queue<Consumer<ToggleableModule>> disableListeners = new ArrayDeque<>();
    private boolean enabled;

    public ToggleableModule(String name) {
        super(name);
    }

    protected void onEnable() {
        this.enableListeners.forEach(listener -> listener.accept(this));
    }

    protected void onDisable() {
        this.disableListeners.forEach(listener -> listener.accept(this));
    }

    public boolean addEnableListener(Consumer<ToggleableModule> enableListener) {
        return this.enableListeners.add(enableListener);
    }

    public boolean removeEnableListener(Consumer<ToggleableModule> enableListener) {
        return this.enableListeners.remove(enableListener);
    }

    public boolean addDisableListener(Consumer<ToggleableModule> disableListener) {
        return this.disableListeners.add(disableListener);
    }

    public boolean removeDisableListener(Consumer<ToggleableModule> disableListener) {
        return this.disableListeners.remove(disableListener);
    }

    @Override
    public boolean toggle() {
        if (this.enabled = !this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }

        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}