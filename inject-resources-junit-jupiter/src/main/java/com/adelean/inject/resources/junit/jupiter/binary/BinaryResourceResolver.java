package com.adelean.inject.resources.junit.jupiter.binary;

import static com.adelean.inject.resources.core.InjectResources.resource;

import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.core.ResourceAsByteArray;
import com.adelean.inject.resources.junit.jupiter.GivenBinaryResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceResolver;

public final class BinaryResourceResolver extends ResourceResolver<GivenBinaryResource, ResourceAsByteArray> {
    protected BinaryResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsByteArray resolve(GivenBinaryResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asByteArray();
    }
}
