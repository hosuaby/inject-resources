package com.adelean.inject.resources.core;

/**
 * Wrapper for resource that may read content of that resource as bytes array.
 *
 * @author Alexei KLENIN
 */
public class ResourceAsByteArray implements Parsable<byte[]> {
    private final Resource delegate;

    ResourceAsByteArray(Resource resource) {
        this.delegate = resource;
    }

    /**
     * @return content of resource as bytes array
     */
    public byte[] bytes() {
        return read();
    }

    /**
     * @return content of resource as bytes array
     */
    @Override
    public byte[] read() {
        return delegate.resourceLoader.readAsByteArray(delegate.resourcePath);
    }
}
