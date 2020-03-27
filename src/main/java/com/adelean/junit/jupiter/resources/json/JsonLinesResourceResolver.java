package com.adelean.junit.jupiter.resources.json;

import java.nio.charset.Charset;

import com.adelean.junit.jupiter.resources.GivenJsonLinesResource;
import com.adelean.junit.jupiter.resources.core.ResourceResolver;
import com.adelean.junit.jupiter.resources.core.helpers.Annotations;
import com.adelean.junit.jupiter.resources.dsl.Resource;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsReader;

public final class JsonLinesResourceResolver extends ResourceResolver<GivenJsonLinesResource, ResourceAsReader> {
    protected JsonLinesResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenJsonLinesResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return Resource
                .onClassLoaderOf(testClass)
                .path(path)
                .asReader(charset);
    }
}
