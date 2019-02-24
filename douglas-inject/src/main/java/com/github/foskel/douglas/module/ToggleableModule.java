package com.github.foskel.douglas.module;

import com.github.foskel.douglas.core.traits.Toggleable;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Fred
 * @since 2/2/2017
 */
public abstract class ToggleableModule extends AbstractModule implements Toggleable {
    private final Queue<Runnable> enableListeners = new ArrayDeque<>();
    private final Queue<Runnable> disableListeners = new ArrayDeque<>();
    private boolean enabled;

    public ToggleableModule(String name) {
        super(name);
    }

    protected void onEnable() {
        this.enableListeners.forEach(Runnable::run);
    }

    protected void onDisable() {
        this.disableListeners.forEach(Runnable::run);
    }

    public boolean addEnableListener(Runnable enableListener) {
        return this.enableListeners.add(enableListener);
    }

    public boolean removeEnableListener(Runnable enableListener) {
        return this.enableListeners.remove(enableListener);
    }

    public boolean addDisableListener(Runnable disableListener) {
        return this.disableListeners.add(disableListener);
    }

    public boolean removeDisableListener(Runnable disableListener) {
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