package com.adelean.junit.jupiter.resources.core;

import java.lang.annotation.Annotation;

public abstract class ResourceResolver<A extends Annotation, R extends Parsable<?>> {
    protected final Class<?> testClass;

    protected ResourceResolver(Class<?> testClass) {
        this.testClass = testClass;
    }

    public abstract R resolve(A resourceAnnotation);
}
