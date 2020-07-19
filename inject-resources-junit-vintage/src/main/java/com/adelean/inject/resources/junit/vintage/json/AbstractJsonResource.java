package com.adelean.inject.resources.junit.vintage.json;

import com.adelean.inject.resources.core.function.ThrowingFunction;
import com.adelean.inject.resources.junit.vintage.core.AbstractTextResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Objects;

public abstract class AbstractJsonResource<T> extends AbstractTextResource<T> {
    private static final String JACKSON_CLASS_NAME = "com.fasterxml.jackson.databind.ObjectMapper";
    private static final String GSON_CLASS_NAME = "com.google.gson.Gson";

    private static final String ERR_WRONG_PARSER_TYPE =
            "Wrong parser type: %s.\nAccepted parser types:"
                    + "\n\t- " + JACKSON_CLASS_NAME
                    + "\n\t- " + GSON_CLASS_NAME;

    protected final Object parser;

    protected AbstractJsonResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
        this.parser = null;
    }

    protected AbstractJsonResource(CodeAnchor codeAnchor, String path, Charset charset, Object parser) {
        super(codeAnchor, path, charset);
        this.parser = parser;
    }

    @NotNull
    protected Object assertHasParser() {
        return Objects.requireNonNull(
                parser, String.format("Rule %s must have defined parser.", getClass().getSimpleName()));
    }

    public abstract <U> AbstractJsonResource<U> parseWith(Object parser);

    protected static <U> ThrowingFunction<Reader, U> parseFunction(Object parser, Type targetType) {
        String parserClassName = parser.getClass().getName();

        if (JACKSON_CLASS_NAME.equals(parserClassName)) {
            return new ParseWithJackson<>(parser, targetType);
        } else if (GSON_CLASS_NAME.equals(parserClassName)) {
            return new ParseWithGson<>(parser, targetType);
        } else {
            throw new IllegalArgumentException(String.format(ERR_WRONG_PARSER_TYPE, parserClassName));
        }
    }
}
