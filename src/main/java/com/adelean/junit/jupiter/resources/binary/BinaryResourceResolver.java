package com.adelean.junit.jupiter.resources.binary;

import com.adelean.junit.jupiter.resources.GivenBinaryResource;
import com.adelean.junit.jupiter.resources.core.ResourceResolver;
import com.adelean.junit.jupiter.resources.core.helpers.Annotations;
import com.adelean.junit.jupiter.resources.dsl.Resource;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsByteArray;

public final class BinaryResourceResolver extends ResourceResolver<GivenBinaryResource, ResourceAsByteArray> {
    protected BinaryResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsByteArray resolve(GivenBinaryResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);

        return Resource
                .onClassLoaderOf(testClass)
                .path(path)
                .asByteArray();
    }
}
