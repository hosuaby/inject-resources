package com.adelean.junit.jupiter.resources.common;

import java.util.HashSet;
import java.util.Set;

import org.junit.platform.commons.support.ReflectionSupport;

import com.adelean.junit.jupiter.resources.TestResourcesExtension;

public final class JacksonPresent {
    private static final Set<String> JACKSON_CLASSES = new HashSet<>();
    static {
        JACKSON_CLASSES.add("com.fasterxml.jackson.databind.ObjectMapper");
        JACKSON_CLASSES.add("com.fasterxml.jackson.core.JsonGenerator");
    }

    private static boolean JACKSON_PRESENT = ReflectionSupport
            .findAllClassesInPackage(
                    TestResourcesExtension.class.getPackage().getName(),
                    any -> true,
                    JACKSON_CLASSES::contains)
            .size() == JACKSON_CLASSES.size();

    public static boolean isJackson2Present() {
        return JACKSON_PRESENT;
    }

    private JacksonPresent() {
    }
}
