package com.adelean.junit.jupiter.resources.core.function;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T t) throws Throwable;
}
