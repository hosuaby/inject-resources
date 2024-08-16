package io.hosuaby.inject.resources.junit.jupiter.yaml;

import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.core.helpers.StringUtils;
import io.hosuaby.inject.resources.junit.jupiter.GivenYamlResource;
import io.hosuaby.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;

public final class YamlResourcesInjector extends AbstractYamlResourcesInjector<GivenYamlResource> {
    private final YamlResourceResolver resourceResolver;

    public YamlResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenYamlResource.class);
        this.resourceResolver = new YamlResourceResolver(testClass);
    }

    @Override
    public Object valueToInject(Type valueType, GivenYamlResource resourceAnnotation) {
        SnakeYamlResourceParser parser = findSnakeYamlParser(resourceAnnotation);

        try (ResourceAsReader resource = resourceResolver.resolve(resourceAnnotation)) {
            return parser.parse(resource, valueType);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    @Nullable
    protected String yamlParserName(GivenYamlResource resourceAnnotation) {
        return StringUtils.blankToNull(resourceAnnotation.yaml());
    }
}
