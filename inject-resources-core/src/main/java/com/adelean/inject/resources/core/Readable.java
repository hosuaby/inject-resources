package com.adelean.inject.resources.core;

@FunctionalInterface
public interface Readable<T> {
    T read();
}
