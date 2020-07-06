package com.adelean.inject.resources.core;

import com.adelean.inject.resources.core.function.ThrowingConsumer;
import com.adelean.inject.resources.core.function.ThrowingFunction;

import java.io.BufferedReader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for resource that may open that resource as stream of text lines.
 *
 * @author Alexei KLENIN
 */
public class ResourceAsLines implements AutoCloseable {
    private final ResourceAsReader delegate;

    @Nullable
    private ThrowingConsumer<String> firstLineConsumer;

    ResourceAsLines(ResourceAsReader delegate) {
        this.delegate = delegate;
    }

    /**
     * Defines hook that is executed when first line of resource was read. Useful for processing of headers.
     *
     * @param firstLineConsumer  consumer of the first line from resource
     * @return this {@code ResourceAsLines} wrapper
     */
    public ResourceAsLines onFirstLine(Consumer<String> firstLineConsumer) {
        this.firstLineConsumer = firstLineConsumer::accept;
        return this;
    }

    /**
     * Defines hook that is executed when first line of resource was read. Hook may throw an exception Useful for
     * processing of headers.
     *
     * @param firstLineConsumer  consumer of the first line from resource. May throw an exception
     * @return this {@code ResourceAsLines} wrapper
     */
    public ResourceAsLines onFirstLineChecked(ThrowingConsumer<String> firstLineConsumer) {
        this.firstLineConsumer = firstLineConsumer;
        return this;
    }

    /**
     * Returns stream of text lines from wrapped resource.
     *
     * <p>If {@link #firstLineConsumer} is not null, the first line will be excluded from returned stream. First line
     * will be processed by {@link #firstLineConsumer} instead.</p>
     *
     * @return steam of text lines from resource
     */
    public Stream<String> lines() {
        BufferedReader reader = delegate.reader();

        if (firstLineConsumer != null) {
            try {
                String firstLine = reader.readLine();
                firstLineConsumer.accept(firstLine);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        return reader
                .lines()
                .onClose(this::checkedClose);
    }

    /**
     * Applies {@code parsingFunction} to each text line from resource and returns the resulting stream.
     *
     * <p>If {@link #firstLineConsumer} is not null, the first line will be excluded from processed stream. First line
     * will be processed by {@link #firstLineConsumer} instead.</p>
     *
     * @param parsingFunction  function that parses a single text line
     * @param <T>  type of elements of result stream
     * @return stream of results of {@code parsingFunction}
     */
    public <T> Stream<T> parseLines(Function<String, T> parsingFunction) {
        try {
            return lines()
                    .map(Line::new)
                    .map(line -> line.parse(parsingFunction));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Applies {@code parsingFunction} to each text line from resource and returns the resulting stream.
     * {@code parsingFunction} may throw an exception.
     *
     * <p>If {@link #firstLineConsumer} is not null, the first line will be excluded from processed stream. First line
     * will be processed by {@link #firstLineConsumer} instead.</p>
     *
     * @param parsingFunction  function that parses a single text line. May throw an exception
     * @param <T>  type of elements of result stream
     * @return stream of results of {@code parsingFunction}
     */
    public <T> Stream<T> parseLinesChecked(ThrowingFunction<String, T> parsingFunction) {
        try {
            return lines()
                    .map(Line::new)
                    .map(line -> line.parseChecked(parsingFunction));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Applies {@code lineConsumer} to each text line from resource.
     *
     * <p>If {@link #firstLineConsumer} is not null, the first line will be excluded from processed stream. First line
     * will be processed by {@link #firstLineConsumer} instead.</p>
     *
     * @param lineConsumer  consumer of a single line
     */
    public void forEachLine(Consumer<String> lineConsumer) {
        try {
            lines().forEach(lineConsumer);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Applies {@code lineConsumer} to each text line from resource. {@code lineConsumer} may throw an exception.
     *
     * <p>If {@link #firstLineConsumer} is not null, the first line will be excluded from processed stream. First line
     * will be processed by {@link #firstLineConsumer} instead.</p>
     *
     * @param lineConsumer  consumer of a single line. May throw an exception
     */
    public void forEachLineChecked(ThrowingConsumer<String> lineConsumer) {
        try {
            lines()
                    .map(Line::new)
                    .forEach(line -> line.thenChecked(lineConsumer));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    private void checkedClose() {
        try {
            this.close();
        } catch (Exception closeException) {
            throw new RuntimeException(closeException);
        }
    }

    private static final class Line implements Parsable<String> {
        private final String line;

        public Line(String line) {
            this.line = line;
        }

        @Override
        public String get() {
            return line;
        }
    }
}
