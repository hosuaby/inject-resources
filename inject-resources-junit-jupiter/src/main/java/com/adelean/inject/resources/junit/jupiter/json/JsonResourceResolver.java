package com.adelean.inject.resources.junit.jupiter.json;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceResolver;

public final class JsonResourceResolver extends ResourceResolver<GivenJsonResource, ResourceAsReader> {
    protected JsonResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenJsonResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
