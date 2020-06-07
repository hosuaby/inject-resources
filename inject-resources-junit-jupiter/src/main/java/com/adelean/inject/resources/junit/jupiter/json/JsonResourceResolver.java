package com.adelean.inject.resources.junit.jupiter.json;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceResolver;
import com.adelean.inject.resources.junit.jupiter.core.helpers.Annotations;

public final class JsonResourceResolver extends ResourceResolver<GivenJsonResource, ResourceAsReader> {
    protected JsonResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenJsonResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
