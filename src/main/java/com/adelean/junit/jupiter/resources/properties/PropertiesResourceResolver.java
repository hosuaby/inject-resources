package com.adelean.junit.jupiter.resources.properties;

import java.nio.charset.Charset;

import com.adelean.junit.jupiter.resources.GivenPropertiesResource;
import com.adelean.junit.jupiter.resources.core.ResourceResolver;
import com.adelean.junit.jupiter.resources.core.helpers.Annotations;
import com.adelean.junit.jupiter.resources.dsl.Resource;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsReader;

public final class PropertiesResourceResolver extends ResourceResolver<GivenPropertiesResource, ResourceAsReader> {
    protected PropertiesResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsReader resolve(GivenPropertiesResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return Resource
                .onClassLoaderOf(testClass)
                .path(path)
                .asReader(charset);
    }
}
