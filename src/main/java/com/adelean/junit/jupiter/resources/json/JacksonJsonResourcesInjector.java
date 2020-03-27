package com.adelean.junit.jupiter.resources.json;

import com.adelean.junit.jupiter.resources.GivenJsonResource;
import com.adelean.junit.jupiter.resources.core.ResourceParser;
import com.adelean.junit.jupiter.resources.core.cdi.InjectionContext;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsReader;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public final class JacksonJsonResourcesInjector extends AbstractJacksonResourcesInjector<GivenJsonResource> {
    private final JsonResourceResolver resourceResolver;

    public JacksonJsonResourcesInjector(
            InjectionContext injectionContext,
            Object testInstance,
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
