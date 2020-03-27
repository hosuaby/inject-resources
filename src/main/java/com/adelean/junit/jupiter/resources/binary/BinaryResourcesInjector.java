package com.adelean.junit.jupiter.resources.binary;

import com.adelean.junit.jupiter.resources.GivenBinaryResource;
import com.adelean.junit.jupiter.resources.core.AbstractResourcesInjector;
import com.adelean.junit.jupiter.resources.core.cdi.InjectionContext;

import java.lang.reflect.Type;

public final class BinaryResourcesInjector extends AbstractResourcesInjector<GivenBinaryResource> {
    private final BinaryResourceResolver resourceResolver;

    public BinaryResourcesInjector(
            InjectionContext injectionContext,
            Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenBinaryResource.class);
        this.resourceResolver = new BinaryResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenBinaryResource resourceAnnotation) {
        return resourceResolver.resolve(resourceAnnotation).bytes();
    }
}
