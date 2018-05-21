package com.github.foskel.douglas.plugin.dependency.supply;

public interface DependencySupplyingStrategy {
    void scanAndSupply(Object source) throws DependencySupplyingException;
}