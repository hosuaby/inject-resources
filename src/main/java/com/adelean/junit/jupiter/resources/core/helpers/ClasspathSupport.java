package com.adelean.junit.jupiter.resources.core.helpers;

import com.adelean.junit.jupiter.resources.TestWithResourcesExtension;

public final class ClasspathSupport {
    private static final ClassLoader CLASS_LOADER = TestWithResourcesExtension.class.getClassLoader();

    private static boolean JACKSON_PRESENT =
            isClassPresent("com.fasterxml.jackson.databind.ObjectMapper")
            && isClassPresent("com.fasterxml.jackson.core.JsonGenerator");

    private static boolean GSON_PRESENT = isClassPresent("com.google.gson.Gson");

    private ClasspathSupport() {
    }

    public static boolean isJackson2Present() {
        return JACKSON_PRESENT;
    }

    public static boolean isGsonPresent() {
        return GSON_PRESENT;
    }

    static boolean isClassPresent(String className) {
        try {
            Class.forName(className, false, CLASS_LOADER);
            return true;
        } catch (ClassNotFoundException classNotFoundException) {
            return false;
        }
    }
}
