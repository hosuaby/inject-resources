package com.adelean.inject.resources.core;

import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.adelean.inject.resources.core.helpers.ResourceLoader;
import com.adelean.inject.resources.core.helpers.StringUtils;

/**
 * Builder for resource that can be read in variety of ways.
 *
 * @author Alexei KLENIN
 */
public class Resource {
    protected final ResourceLoader resourceLoader;
    protected final String resourcePath;

    Resource(ResourceLoader resourceLoader, String resourcePath) {
        this.resourceLoader = resourceLoader;
        this.resourcePath = resourcePath;
    }

    /**
     * Returns resource wrapper {@link ResourceAsInputStream} that opens resource as {@link java.io.InputStream}.
     *
     * @return wrapper that opens resource as input stream
     */
    public ResourceAsInputStream asInputStream() {
        return new ResourceAsInputStream(this);
    }

    /**
     * Returns resource wrapper {@link ResourceAsByteArray} that reads resource as bytes array.
     *
     * @return wrapper that reads resource as bytes array
     */
    public ResourceAsByteArray asByteArray() {
        return new ResourceAsByteArray(this);
    }

    /**
     * Returns content of resource file as bytes array.
     *
     * @return content as bytes array
     */
    public byte[] bytes() {
        return new ResourceAsByteArray(this).bytes();
    }

    /**
     * Returns resource wrapper {@link ResourceAsReader} that opens resource as {@link java.io.Reader} with default
     * charset.
     *
     * @return wrapper that opens resource as reader
     */
    public ResourceAsReader asReader() {
        return new ResourceAsReader(this);
    }

    /**
     * Returns resource wrapper {@link ResourceAsReader} that opens resource as {@link java.io.Reader} with specified
     * charset.
     *
     * @param charset  text resource charset
     * @return wrapper that opens resource as reader
     */
    public ResourceAsReader asReader(Charset charset) {
        return new ResourceAsReader(this, charset);
    }

    /**
     * Returns resource wrapper {@link ResourceAsLines} that opens resource as stream of text lines with default
     * charset.
     *
     * @return wrapper that opens resource as stream of text lines
     */
    public ResourceAsLines asLines() {
        return new ResourceAsReader(this).asLines();
    }

    /**
     * Returns resource wrapper {@link ResourceAsLines} that opens resource as stream of text lines with specified
     * charset.
     *
     * @param charset  text resource charset
     * @return wrapper that opens resource as stream of text lines
     */
    public ResourceAsLines asLines(Charset charset) {
        return new ResourceAsReader(this, charset).asLines();
    }

    /**
     * Returns resource wrapper {@link ResourceAsText} that reads resources as {@link String} with default charset.
     *
     * @return wrapper that reads resource as text
     */
    public ResourceAsText asText() {
        return new ResourceAsText(this);
    }

    /**
     * Returns resource wrapper {@link ResourceAsText} that reads resources as {@link String} with specified charset.
     *
     * @param charset  text resource charset
     * @return wrapper that reads resource as text
     */
    public ResourceAsText asText(Charset charset) {
        return new ResourceAsText(this, charset);
    }

    /**
     * Returns content of resource file as {@link String} using with default charset.
     *
     * @return content as text
     */
    public String text() {
        return new ResourceAsText(this).text();
    }

    /**
     * Returns content of resource file as {@link String} using with specified charset.
     *
     * @param charset  text resource charset
     * @return content as text
     */
    public String text(Charset charset) {
        return new ResourceAsText(this, charset).text();
    }

    /**
     * Builder for resource that is present on classpath of supplied {@link ClassLoader}.
     */
    public static final class ResourceOnClassloader {
        private final ResourceLoader resourceLoader;

        ResourceOnClassloader(ClassLoader classLoader) {
            this.resourceLoader = new ResourceLoader(classLoader);
        }

        /**
         * Returns builder for resource with defined {@code path} on a classpath of classloader of this builder.
         *
         * <p>Those two operations are equivalent:</p>
         *
         * <pre>
         *     withPath("/com/adelean/junit/jupiter/resource.txt")
         *
         *     // equivalent to
         *
         *     withPath("/com/adelean/junit/jupiter", "resource.txt")
         * </pre>
         *
         * @param firstPathToken  path of resource, it least one token
         * @param otherTokens  path of resource, allows multiple tokens
         * @return {@link Resource} resource builder
         */
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
