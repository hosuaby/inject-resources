package com.adelean.inject.resources.core;

import com.adelean.inject.resources.core.function.ThrowingConsumer;
import com.adelean.inject.resources.core.function.ThrowingFunction;

import java.io.BufferedReader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

public class ResourceAsLines implements AutoCloseable {
    private final ResourceAsReader delegate;

    @Nullable
    private ThrowingConsumer<String> firstLineConsumer;

    ResourceAsLines(ResourceAsReader delegate) {
        this.delegate = delegate;
    }

    public ResourceAsLines onFirstLine(Consumer<String> lineConsumer) {
        this.firstLineConsumer = lineConsumer::accept;
        return this;
    }

    public ResourceAsLines onFirstLineChecked(ThrowingConsumer<String> lineConsumer) {
        this.firstLineConsumer = lineConsumer;
        return this;
    }

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

    public <T> Stream<T> parseLines(Function<String, T> parsingFunction) {
        try {
            return lines()
                    .map(Line::new)
                    .map(line -> line.parse(parsingFunction));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public <T> Stream<T> parseLinesChecked(ThrowingFunction<String, T> parsingFunction) {
        try {
            return lines()
                    .map(Line::new)
                    .map(line -> line.parseChecked(parsingFunction));
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void forEachLine(Consumer<String> lineConsumer) {
        try {
            lines().forEach(lineConsumer);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

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
        public String read() {
            return line;
        }
    }
}
