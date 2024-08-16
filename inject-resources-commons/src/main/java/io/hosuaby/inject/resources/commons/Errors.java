package io.hosuaby.inject.resources.commons;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Errors {
    private static final String INTERNAL_ERROR_TEMPLATE =
            "INTERNAL ERROR: %s. Please, open an issue on Github repository of project 'junit-jupiter-resources'.";

    private Errors() {
    }

    public static String internalError(String message) {
        return String.format(INTERNAL_ERROR_TEMPLATE, message);
    }

    public static String typesToString(Class<?>... types) {
        return Stream
                .of(types)
                .map(type -> String.format("\t- %s", type.getName()))
                .collect(Collectors.joining("\n"));
    }
}
