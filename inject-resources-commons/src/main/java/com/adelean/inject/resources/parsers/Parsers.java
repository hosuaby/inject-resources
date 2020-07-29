package com.adelean.inject.resources.parsers;

import static com.adelean.inject.resources.commons.ClasspathSupport.GSON_CLASS_NAME;
import static com.adelean.inject.resources.commons.ClasspathSupport.JACKSON_MAPPER_CLASS_NAME;

import com.adelean.inject.resources.core.function.ThrowingFunction;

import java.io.Reader;
import java.lang.reflect.Type;

public final class Parsers {
    private static final String ERR_WRONG_PARSER_TYPE =
            "Wrong parser type: %s.\nAccepted parser types:"
                    + "\n\t- " + JACKSON_MAPPER_CLASS_NAME
                    + "\n\t- " + GSON_CLASS_NAME;

    private Parsers() {
    }

    public static <U> ThrowingFunction<Reader, U> parseFunction(Object parser, Type targetType) {
        String parserClassName = parser.getClass().getName();

        if (JACKSON_MAPPER_CLASS_NAME.equals(parserClassName)) {
            return new ParseWithJackson<>(parser, targetType);
        } else if (GSON_CLASS_NAME.equals(parserClassName)) {
            return new ParseWithGson<>(parser, targetType);
        } else {
            throw new IllegalArgumentException(String.format(ERR_WRONG_PARSER_TYPE, parserClassName));
        }
    }
}
