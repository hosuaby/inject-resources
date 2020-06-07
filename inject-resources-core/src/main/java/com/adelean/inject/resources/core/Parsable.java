package com.adelean.inject.resources.core;

import com.adelean.inject.resources.core.function.ThrowingConsumer;
import com.adelean.inject.resources.core.function.ThrowingFunction;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Parsable<I> extends Readable<I> {
    default <O> O parse(Function<I, O> parsingFunction) {
        return parsingFunction.apply(read());
    }

    default <O> O parseChecked(ThrowingFunction<I, O> parsingFunction) {
        I content = read();

        try {
            return parsingFunction.apply(content);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            if (content instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) content).close();
                } catch (Exception closeException) {
                    throw new RuntimeException(closeException);
                }
            }
        }
    }

    default void then(Consumer<I> contentConsumer) {
        contentConsumer.accept(read());
    }

    default void thenChecked(ThrowingConsumer<I> contentConsumer) {
        I content = read();

        try {
            contentConsumer.accept(read());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            if (content instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) content).close();
                } catch (Exception closeException) {
                    throw new RuntimeException(closeException);
                }
            }
        }
    }
}
