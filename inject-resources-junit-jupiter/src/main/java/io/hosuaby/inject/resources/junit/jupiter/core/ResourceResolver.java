package io.hosuaby.inject.resources.junit.jupiter.core;

import java.lang.annotation.Annotation;

import io.hosuaby.inject.resources.core.Parsable;

public abstract class ResourceResolver<A extends Annotation, R extends Parsable<?>> {
    protected final Class<?> testClass;

    protected ResourceResolver(Class<?> testClass) {
        this.testClass = testClass;
    }

    public abstract R resolve(A resourceAnnotation);
}
