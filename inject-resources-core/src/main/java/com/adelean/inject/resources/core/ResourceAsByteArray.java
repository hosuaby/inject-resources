package com.adelean.inject.resources.core;

public class ResourceAsByteArray implements Parsable<byte[]> {
    private final Resource delegate;

    ResourceAsByteArray(Resource resource) {
        this.delegate = resource;
    }

    public byte[] bytes() {
        return read();
    }

    @Override
    public byte[] read() {
        return delegate.resourceLoader.readAsByteArray(delegate.resourcePath);
    }
}
