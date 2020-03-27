package com.adelean.junit.jupiter.resources.core.helpers;

public final class StringUtils {
    private StringUtils() {
    }

    public static String ucfirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
