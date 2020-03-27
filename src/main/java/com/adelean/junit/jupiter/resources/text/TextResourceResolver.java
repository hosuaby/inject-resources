package com.adelean.junit.jupiter.resources.text;

import java.nio.charset.Charset;

import com.adelean.junit.jupiter.resources.GivenTextResource;
import com.adelean.junit.jupiter.resources.core.ResourceResolver;
import com.adelean.junit.jupiter.resources.core.helpers.Annotations;
import com.adelean.junit.jupiter.resources.dsl.Resource;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsText;

public final class TextResourceResolver extends ResourceResolver<GivenTextResource, ResourceAsText> {
    protected TextResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsText resolve(GivenTextResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return Resource
                .onClassLoaderOf(testClass)
                .path(path)
                .asText(charset);
    }
}
