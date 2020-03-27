package com.adelean.junit.jupiter.resources.json;

import java.nio.charset.Charset;

import com.adelean.junit.jupiter.resources.GivenJsonResource;
import com.adelean.junit.jupiter.resources.core.ResourceResolver;
import com.adelean.junit.jupiter.resources.core.helpers.Annotations;
import com.adelean.junit.jupiter.resources.dsl.Resource;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsReader;

public final class JsonResourceResolver extends ResourceResolver<GivenJsonResource, ResourceAsReader> {
    protected JsonResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenJsonResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return Resource
                .onClassLoaderOf(testClass)
                .path(path)
                .asReader(charset);
    }
}
