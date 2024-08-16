package io.hosuaby.inject.resources.junit.jupiter.properties;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.jupiter.GivenPropertiesResource;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceResolver;

public final class PropertiesResourceResolver extends ResourceResolver<GivenPropertiesResource, ResourceAsReader> {
    protected PropertiesResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenPropertiesResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
