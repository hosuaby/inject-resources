package com.adelean.inject.resources.junit.vintage.json;

import com.adelean.inject.resources.junit.vintage.core.AbstractTextResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.Objects;

public abstract class AbstractJsonResource<T> extends AbstractTextResource<T> {
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

    /**
     * Defines object used to parse content of this JSON resource. Can be Jackson {@code ObjectMapper} or Google Gson
     * {@code Gson}.
     *
     * <p>Parser must be provided during rule building, or exception will be thrown during resource loading.
     *
     * @param parser  Jackson {@code ObjectMapper} or Google Gson {@code Gson} object
     * @param <U>  type to which resource content will be converted
     * @return this rule
     */
    public abstract <U> AbstractJsonResource<U> parseWith(Object parser);
}
