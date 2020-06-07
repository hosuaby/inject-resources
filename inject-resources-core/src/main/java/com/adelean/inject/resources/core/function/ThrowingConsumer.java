package com.adelean.inject.resources.core.function;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T t) throws Throwable;
}
