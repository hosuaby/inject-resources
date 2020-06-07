package com.adelean.inject.resources.core;

import java.io.IOException;
import java.io.InputStream;

public class ResourceAsInputStream implements Parsable<InputStream>, AutoCloseable {
    private final Resource delegate;
    private InputStream inputStream;

    ResourceAsInputStream(Resource resource) {
        this.delegate = resource;
    }

    public InputStream inputStream() {
        return read();
    }

    @Override
    public InputStream read() {
        inputStream = delegate.resourceLoader.resourceStream(delegate.resourcePath);
        return inputStream;
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
