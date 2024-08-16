package io.hosuaby.inject.resources.junit.jupiter.properties;

import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.jupiter.GivenPropertiesResource;
import io.hosuaby.inject.resources.junit.jupiter.core.AbstractResourcesInjector;
import io.hosuaby.inject.resources.junit.jupiter.core.cdi.InjectionContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

import org.jetbrains.annotations.Nullable;

public final class PropertiesResourcesInjector extends AbstractResourcesInjector<GivenPropertiesResource> {
    private final PropertiesResourceResolver resourceResolver;

    public PropertiesResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
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
