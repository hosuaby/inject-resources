package com.adelean.inject.resources.junit.jupiter.properties;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenPropertiesResource;
import com.adelean.inject.resources.junit.jupiter.core.AbstractResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

public final class PropertiesResourcesInjector extends AbstractResourcesInjector<GivenPropertiesResource> {
    private final PropertiesResourceResolver resourceResolver;

    public PropertiesResourcesInjector(
            InjectionContext injectionContext,
            Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenPropertiesResource.class);
        this.resourceResolver = new PropertiesResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenPropertiesResource resourceAnnotation) {
        Properties properties = new Properties();

        try (ResourceAsReader resource = resourceResolver.resolve(resourceAnnotation)) {
            resource.thenChecked(properties::load);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        return properties;
    }
}
