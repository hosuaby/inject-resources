package com.adelean.inject.resources.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Wrapper for resource that may open that resource as {@link Reader} with defined {@link #charset}.
 *
 * @author Alexei KLENIN
 */
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

    /**
     * @return {@link Reader} from resource.
     */
    public BufferedReader reader() {
        return delegate.resourceLoader.resourceReader(delegate.resourcePath, charset);
    }

    /**
     * @return {@link Reader} from resource.
     */
    @Override
    public BufferedReader get() {
        return reader();
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * Returns resource wrapper {@link ResourceAsLines} that opens resource as stream of text lines with defined
     * {@link #charset}.
     *
     * @return wrapper that opens resource as stream of text lines
     */
    public ResourceAsLines asLines() {
        return new ResourceAsLines(this);
    }
}
