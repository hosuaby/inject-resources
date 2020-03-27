package com.adelean.junit.jupiter.resources.dsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.adelean.junit.jupiter.resources.core.Parsable;

public class ResourceAsReader implements Parsable<Reader>, AutoCloseable {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Resource delegate;
    private final Charset charset;
    private BufferedReader reader;

    ResourceAsReader(Resource resource) {
        this(resource, DEFAULT_CHARSET);
    }

    ResourceAsReader(Resource resource, Charset charset) {
        this.delegate = resource;
        this.charset = charset;
    }

    public BufferedReader reader() {
        return read();
    }

    @Override
    public BufferedReader read() {
        reader = delegate.resourceLoader.resourceReader(delegate.resourcePath, charset);
        return reader;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
