package com.adelean.junit.jupiter.resources.core.function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Throwable;
}
