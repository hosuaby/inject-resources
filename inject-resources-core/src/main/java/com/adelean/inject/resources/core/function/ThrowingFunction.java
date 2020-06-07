package com.adelean.inject.resources.core.function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Throwable;
}
