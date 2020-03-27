package com.adelean.junit.jupiter.resources.text;

import com.adelean.junit.jupiter.resources.GivenTextResource;
import com.adelean.junit.jupiter.resources.core.AbstractResourcesInjector;
import com.adelean.junit.jupiter.resources.core.cdi.InjectionContext;

import java.lang.reflect.Type;

public final class TextResourcesInjector extends AbstractResourcesInjector<GivenTextResource> {
    private final TextResourceResolver resourceResolver;

    public TextResourcesInjector(
            InjectionContext injectionContext,
            Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenTextResource.class);
        this.resourceResolver = new TextResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenTextResource resourceAnnotation) {
        return resourceResolver.resolve(resourceAnnotation).text();
    }
}
