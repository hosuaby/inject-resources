package com.adelean.junit.jupiter.resources.dsl;

import com.adelean.junit.jupiter.resources.core.helpers.ResourceLoader;
import org.junit.platform.commons.util.StringUtils;

import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resource {
    protected final ResourceLoader resourceLoader;
    protected final String resourcePath;

    Resource(ResourceLoader resourceLoader, String resourcePath) {
        this.resourceLoader = resourceLoader;
        this.resourcePath = resourcePath;
    }

    public static ResourceOnClassloader onClassLoaderOf(Class<?> clazz) {
        return onClassLoader(clazz.getClassLoader());
    }

    public static ResourceOnClassloader onClassLoader(ClassLoader classLoader) {
        return new ResourceOnClassloader(classLoader);
    }

    public static Resource path(String... pathTokens) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return new ResourceOnClassloader(contextClassLoader).path(pathTokens);
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

    public <O> ResourceAsText asText() {
        return new ResourceAsText(this);
    }

    public <O> ResourceAsText asText(Charset charset) {
        return new ResourceAsText(this, charset);
    }

    public static final class ResourceOnClassloader {
        private final ResourceLoader resourceLoader;

        private ResourceOnClassloader(ClassLoader classLoader) {
            this.resourceLoader = new ResourceLoader(classLoader);
        }

        public Resource path(String... pathTokens) {
            String resourcePath = buildPath(pathTokens);
            return new Resource(resourceLoader, resourcePath);
        }
    }

    static String buildPath(String... pathTokens) {
        return Stream
                .of(pathTokens)
                .map(token -> token.split("/"))
                .flatMap(Stream::of)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("/"));
    }
}
