package com.adelean.inject.resources.junit.vintage.core;

import static com.adelean.inject.resources.core.Resource.buildPath;

import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Alexei KLENIN
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

    public abstract <U> AbstractTextResource<U> withCharset(Charset charset);
}
