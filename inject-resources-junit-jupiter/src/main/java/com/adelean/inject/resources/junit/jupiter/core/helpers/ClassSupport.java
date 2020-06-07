package com.adelean.inject.resources.junit.jupiter.core.helpers;

public final class ClassSupport {
    private ClassSupport() {
    }

    public static boolean isSubclass(Class<?> clazz, Class<?> superClass) {
        return clazz != superClass && superClass.isAssignableFrom(clazz);
    }
}
