package com.github.idkp.douglas.util;

import java.util.Optional;

public final class Exceptions {

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwAsUnchecked(Throwable exception, Object dummy) throws T {
        throw (T) exception;
    }

    public static void throwAsUnchecked(Throwable throwable) {
        throwAsUnchecked(throwable, null);
    }

    public static <T> T getOrThrowAsUnchecked(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> target, Throwable throwable) {
        if (!target.isPresent()) {
            throwAsUnchecked(throwable);
        }

        return target.get();
    }
}