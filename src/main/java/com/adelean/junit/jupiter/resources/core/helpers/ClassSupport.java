package com.adelean.junit.jupiter.resources.core.helpers;

public final class ClassSupport {
    private ClassSupport() {
    }

    public static boolean isSubclass(Class<?> clazz, Class<?> superClass) {
        return clazz != superClass && superClass.isAssignableFrom(clazz);
    }
}
