package com.adelean.inject.resources.junit.jupiter.json;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenJsonLinesResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceResolver;
import com.adelean.inject.resources.junit.jupiter.core.helpers.Annotations;

public final class JsonLinesResourceResolver extends ResourceResolver<GivenJsonLinesResource, ResourceAsReader> {
    protected JsonLinesResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenJsonLinesResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
