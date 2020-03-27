package com.adelean.junit.jupiter.resources.core;

import java.util.function.Consumer;
import java.util.function.Function;

import com.adelean.junit.jupiter.resources.core.function.ThrowingConsumer;
import com.adelean.junit.jupiter.resources.core.function.ThrowingFunction;

public interface Parsable<I> extends Readable<I> {
    default <O> O parse(Function<I, O> parsingFunction) {
        return parsingFunction.apply(read());
    }

    default <O> O parseChecked(ThrowingFunction<I, O> parsingFunction) {
        try {
            return parsingFunction.apply(read());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    default void then(Consumer<I> contentConsumer) {
        contentConsumer.accept(read());
    }

    default void thenChecked(ThrowingConsumer<I> contentConsumer) {
        try {
            contentConsumer.accept(read());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
