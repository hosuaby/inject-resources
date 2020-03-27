package com.adelean.junit.jupiter.resources.core.helpers;

public final class Errors {
    private static final String INTERNAL_ERROR_TEMPLATE =
            "INTERNAL ERROR: %s. Please, open an issue on Github repository of project 'junit-jupiter-resources'.";

    private Errors() {
    }

    public static String internalError(String message) {
        return String.format(INTERNAL_ERROR_TEMPLATE, message);
    }
}
