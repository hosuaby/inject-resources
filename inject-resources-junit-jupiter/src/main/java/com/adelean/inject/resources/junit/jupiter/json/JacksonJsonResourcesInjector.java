package com.adelean.inject.resources.junit.jupiter.json;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.core.ResourceParser;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public final class JacksonJsonResourcesInjector extends AbstractJacksonResourcesInjector<GivenJsonResource> {
    private final JsonResourceResolver resourceResolver;

    public JacksonJsonResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenJsonResource.class);
        this.resourceResolver = new JsonResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenJsonResource resourceAnnotation) {
        ResourceParser<ResourceAsReader, Object> parser = findJsonParser(resourceAnnotation);

        try (ResourceAsReader resource = resourceResolver.resolve(resourceAnnotation)) {
            return parser.parse(resource, valueType);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    boolean isParsedWithJackson(GivenJsonResource resourceAnnotation) {
        return StringUtils.isNotBlank(resourceAnnotation.jacksonMapper());
    }

    @Override
    String jacksonMapperName(GivenJsonResource resourceAnnotation) {
        return resourceAnnotation.jacksonMapper();
    }

    @Override
    boolean isParsedWithGson(GivenJsonResource resourceAnnotation) {
        return StringUtils.isNotBlank(resourceAnnotation.gson());
    }

    @Override
    String gsonName(GivenJsonResource resourceAnnotation) {
        return resourceAnnotation.gson();
    }
}
