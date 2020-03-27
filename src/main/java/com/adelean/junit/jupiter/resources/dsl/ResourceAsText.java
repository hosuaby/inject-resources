package com.adelean.junit.jupiter.resources.dsl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.adelean.junit.jupiter.resources.core.Parsable;

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

    public String text() {
        return read();
    }

    @Override
    public String read() {
        return delegate.resourceLoader.readAsText(delegate.resourcePath, charset);
    }
}
