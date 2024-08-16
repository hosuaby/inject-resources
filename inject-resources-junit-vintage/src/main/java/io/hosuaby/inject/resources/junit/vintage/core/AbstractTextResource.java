package io.hosuaby.inject.resources.junit.vintage.core;

import static io.hosuaby.inject.resources.core.Resource.buildPath;

import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Base implementation of resource containing text.
 *
 * @param <T>  type to which resource content will be converted
 */
public abstract class AbstractTextResource<T> extends AbstractResourceRule<T> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected final Charset charset;

    protected AbstractTextResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        this(codeAnchor, buildPath(firstPathToken, otherTokens), DEFAULT_CHARSET);
    }

    protected AbstractTextResource(CodeAnchor codeAnchor, String path, Charset charset) {
        super(codeAnchor, path);
        this.charset = charset;
    }

    /**
     * Defines charset of represented resource. By default charset of textual resource is assumed to be
     * {@link StandardCharsets#UTF_8}.
     *
     * @param charset  charset of textual resource
     * @param <U>  type to which resource content will be converted
     * @return this rule
     */
    public abstract <U> AbstractTextResource<U> withCharset(Charset charset);
}
