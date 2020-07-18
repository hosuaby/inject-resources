package com.adelean.inject.resources.junit.jupiter.binary;

import com.adelean.inject.resources.junit.jupiter.GivenBinaryResource;
import com.adelean.inject.resources.junit.jupiter.core.AbstractResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;

import java.lang.reflect.Type;

import org.jetbrains.annotations.Nullable;

public final class BinaryResourcesInjector extends AbstractResourcesInjector<GivenBinaryResource> {
    private final BinaryResourceResolver resourceResolver;

    public BinaryResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenBinaryResource.class);
        this.resourceResolver = new BinaryResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenBinaryResource resourceAnnotation) {
        return resourceResolver.resolve(resourceAnnotation).bytes();
    }
}
