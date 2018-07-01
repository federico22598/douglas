package com.github.foskel.douglas.plugin.dependency.supply;

/**
 * @author Foskel
 */
public interface DependencySupplyingStrategy {
    void scanAndSupply(Object source) throws DependencySupplyingException;
}