package com.adelean.inject.resources.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
