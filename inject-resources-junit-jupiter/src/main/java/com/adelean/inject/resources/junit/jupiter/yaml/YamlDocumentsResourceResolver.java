package com.adelean.inject.resources.junit.jupiter.yaml;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceResolver;
import com.adelean.inject.resources.junit.jupiter.core.helpers.Annotations;

public final class YamlDocumentsResourceResolver
        extends ResourceResolver<GivenYamlDocumentsResource, ResourceAsReader> {
    protected YamlDocumentsResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenYamlDocumentsResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset);
    }
}
