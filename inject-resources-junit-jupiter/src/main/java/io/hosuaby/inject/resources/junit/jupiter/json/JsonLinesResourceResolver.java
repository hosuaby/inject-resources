package io.hosuaby.inject.resources.junit.jupiter.json;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.jupiter.GivenJsonLinesResource;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceResolver;

public final class JsonLinesResourceResolver extends ResourceResolver<GivenJsonLinesResource, ResourceAsReader> {
    protected JsonLinesResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenJsonLinesResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
