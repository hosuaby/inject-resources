package io.hosuaby.inject.resources.core;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper for resource that may open that resource as {@link InputStream}.
 *
 * @author Alexei KLENIN
 */
public class ResourceAsInputStream implements Parsable<InputStream>, AutoCloseable {
    private final Resource delegate;
    private InputStream inputStream;

    ResourceAsInputStream(Resource resource) {
        this.delegate = resource;
    }

    /**
     * @return {@link InputStream} from resource.
     */
    public InputStream inputStream() {
        inputStream = delegate.resourceLoader.resourceStream(delegate.resourcePath);
        return inputStream;
    }

    /**
     * @return {@link InputStream} from resource.
     */
    @Override
    public InputStream get() {
        return inputStream();
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
