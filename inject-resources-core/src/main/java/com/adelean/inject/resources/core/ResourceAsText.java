package com.adelean.inject.resources.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Wrapper for resource that may read that resource as {@link String} with defined {@link #charset}.
 *
 * @author Alexei KLENIN
 */
public class ResourceAsText implements Parsable<String> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Resource delegate;
    private final Charset charset;

    ResourceAsText(Resource resource) {
        this(resource, DEFAULT_CHARSET);
    }

    ResourceAsText(Resource resource, Charset charset) {
        this.delegate = resource;
        this.charset = charset;
    }

    /**
     * @return content of resource as {@link String}
     */
    public String text() {
        return delegate.resourceLoader.readAsText(delegate.resourcePath, charset);
    }

    /**
     * @return content of resource as {@link String}
     */
    @Override
    public String get() {
        return text();
    }
}
