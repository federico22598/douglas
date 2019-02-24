//package com.github.foskel.douglas.plugin.impl.dependency.process.supply;
//
//import com.github.foskel.douglas.plugin.dependency.supply.DependencySupplyingStrategy;
//import com.github.foskel.douglas.plugin.locate.PluginLocatorService;
//import com.github.foskel.haptor.process.DependencyProcessor;
//import com.github.foskel.haptor.satisfy.DependencySatisfyingResult;
//
///**
// * @author Foskel
// */
//public final class SupplyingDependencySatisfyingProcessor implements DependencyProcessor {
//    private final DependencySupplyingStrategy supplyingStrategy;
//    private final Object source;
//
//    private SupplyingDependencySatisfyingProcessor(DependencySupplyingStrategy supplyingStrategy, Object source) {
//        this.supplyingStrategy = supplyingStrategy;
//        this.source = source;
//    }
//
//    public static SupplyingDependencySatisfyingProcessor of(PluginLocatorService locatorService, Object source) {
//        return new SupplyingDependencySatisfyingProcessor(
//                new FieldSupplyingStrategy(locatorService), source);
//    }
//
//    @Override
//    public void postSatisfy(DependencySatisfyingResult result) {
//        if (result.getValidationResult()) {
//            this.supplyingStrategy.scanAndSupply(this.source);
//        }
//    }
//}