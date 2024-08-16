package io.hosuaby.inject.resources.junit.jupiter.text;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import java.nio.charset.Charset;

import io.hosuaby.inject.resources.commons.AnnotationSupport;
import io.hosuaby.inject.resources.core.ResourceAsText;
import io.hosuaby.inject.resources.junit.jupiter.GivenTextResource;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceResolver;

public final class TextResourceResolver extends ResourceResolver<GivenTextResource, ResourceAsText> {
    protected TextResourceResolver(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public ResourceAsText resolve(GivenTextResource resourceAnnotation) {
        String path = AnnotationSupport.getFrom(resourceAnnotation);
        Charset charset = Charset.forName(resourceAnnotation.charset());

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asText(charset);
    }
}
