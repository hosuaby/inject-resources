package io.hosuaby.inject.resources.junit.jupiter.binary;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.core.ResourceAsByteArray;
import io.hosuaby.inject.resources.junit.jupiter.GivenBinaryResource;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceResolver;

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
