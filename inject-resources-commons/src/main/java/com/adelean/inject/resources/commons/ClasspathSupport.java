package com.adelean.inject.resources.commons;

public final class ClasspathSupport {
    private static final ClassLoader CLASS_LOADER = ClasspathSupport.class.getClassLoader();

    private static boolean JACKSON_PRESENT =
            isClassPresent("com.fasterxml.jackson.databind.ObjectMapper")
            && isClassPresent("com.fasterxml.jackson.core.JsonGenerator");

    private static boolean GSON_PRESENT = isClassPresent("com.google.gson.Gson");

    private static boolean SNAKE_YAML_PRESENT = isClassPresent("org.yaml.snakeyaml.Yaml");

    private ClasspathSupport() {
    }

    public static boolean isJackson2Present() {
        return JACKSON_PRESENT;
    }

    public static boolean isGsonPresent() {
        return GSON_PRESENT;
    }

    public static boolean isSnakeYamlPresent() {
        return SNAKE_YAML_PRESENT;
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
