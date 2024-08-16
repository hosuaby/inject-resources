package io.hosuaby.inject.resources.junit.jupiter.yaml;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.jupiter.GivenYamlResource;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceResolver;

import java.nio.charset.Charset;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

public final class YamlResourceResolver extends ResourceResolver<GivenYamlResource, ResourceAsReader> {
    protected YamlResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenYamlResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
