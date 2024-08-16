package io.hosuaby.inject.resources.junit.jupiter.text;

import io.hosuaby.inject.resources.junit.jupiter.GivenTextResource;
import io.hosuaby.inject.resources.junit.jupiter.core.AbstractResourcesInjector;
import io.hosuaby.inject.resources.junit.jupiter.core.cdi.InjectionContext;

import java.lang.reflect.Type;

import org.jetbrains.annotations.Nullable;

public final class TextResourcesInjector extends AbstractResourcesInjector<GivenTextResource> {
    private final TextResourceResolver resourceResolver;

    public TextResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenTextResource.class);
        this.resourceResolver = new TextResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenTextResource resourceAnnotation) {
        return resourceResolver.resolve(resourceAnnotation).text();
    }
}
