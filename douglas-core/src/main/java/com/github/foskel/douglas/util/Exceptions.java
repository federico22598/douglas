package com.github.foskel.douglas.util;

/**
 * @author Foskel
 */
public final class Exceptions {

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void castAndThrow(Throwable throwable) throws T {
        throw (T) throwable;
    }

    public static void throwAsUnchecked(Throwable throwable) {
        castAndThrow(throwable);
    }
}