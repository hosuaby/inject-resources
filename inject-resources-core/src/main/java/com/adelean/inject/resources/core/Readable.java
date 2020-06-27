package com.adelean.inject.resources.core;

/**
 * Interface of resource builder that can read resource as value of type {@code T}.
 *
 * @param <T>  type of resource content
 *
 * @author Alexei KLENIN
 */
@FunctionalInterface
public interface Readable<T> {

    /**
     * @return content of resource as type {@code T}.
     */
    T read();
}
