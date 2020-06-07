package com.adelean.inject.resources.junit.jupiter.text;

import static com.adelean.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import com.adelean.inject.resources.core.ResourceAsText;
import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceResolver;
import com.adelean.inject.resources.junit.jupiter.core.helpers.Annotations;

public final class TextResourceResolver extends ResourceResolver<GivenTextResource, ResourceAsText> {
    protected TextResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsText resolve(GivenTextResource resourceAnnotation) {
        String path = Annotations.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asText(charset);
    }
}
