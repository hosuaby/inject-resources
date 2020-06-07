package com.adelean.inject.resources.core.helpers;

public final class StringUtils {
    private StringUtils() {
    }

    public static String ucfirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().isEmpty());
    }
}
