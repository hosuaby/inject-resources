package com.adelean.inject.resources.core;

import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.adelean.inject.resources.core.helpers.ResourceLoader;
import com.adelean.inject.resources.core.helpers.StringUtils;

public class Resource {
    protected final ResourceLoader resourceLoader;
    protected final String resourcePath;

    Resource(ResourceLoader resourceLoader, String resourcePath) {
        this.resourceLoader = resourceLoader;
        this.resourcePath = resourcePath;
    }

    public <O> ResourceAsInputStream asInputStream() {
        return new ResourceAsInputStream(this);
    }

    public <O> ResourceAsByteArray asByteArray() {
        return new ResourceAsByteArray(this);
    }

    public <O> ResourceAsReader asReader() {
        return new ResourceAsReader(this);
    }

    public <O> ResourceAsReader asReader(Charset charset) {
        return new ResourceAsReader(this, charset);
    }

    public <O> ResourceAsLines asLines() {
        return new ResourceAsReader(this).asLines();
    }

    public <O> ResourceAsLines asLines(Charset charset) {
        return new ResourceAsReader(this, charset).asLines();
    }

    public <O> ResourceAsText asText() {
        return new ResourceAsText(this);
    }

    public <O> ResourceAsText asText(Charset charset) {
        return new ResourceAsText(this, charset);
    }

    public static final class ResourceOnClassloader {
        private final ResourceLoader resourceLoader;

        ResourceOnClassloader(ClassLoader classLoader) {
            this.resourceLoader = new ResourceLoader(classLoader);
        }

        public Resource withPath(String firstPathToken, String... otherTokens) {
            String resourcePath = buildPath(firstPathToken, otherTokens);
            return new Resource(resourceLoader, resourcePath);
        }
    }

    static String buildPath(String firstPathToken, String... otherTokens) {
        return Stream
                .concat(Stream.of(firstPathToken), Stream.of(otherTokens))
                .map(token -> token.split("/"))
                .flatMap(Stream::of)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("/"));
    }
}
