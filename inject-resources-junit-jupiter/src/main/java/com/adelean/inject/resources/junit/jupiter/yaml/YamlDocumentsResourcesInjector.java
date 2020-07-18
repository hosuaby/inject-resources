package com.adelean.inject.resources.junit.jupiter.yaml;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.core.helpers.StringUtils;
import com.adelean.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.adelean.inject.resources.junit.jupiter.core.helpers.ClassSupport;
import com.adelean.inject.resources.junit.jupiter.core.helpers.CollectionFactory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.StreamSupport;

public final class YamlDocumentsResourcesInjector extends AbstractYamlResourcesInjector<GivenYamlDocumentsResource> {
    private final YamlDocumentsResourceResolver resourceResolver;

    public YamlDocumentsResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenYamlDocumentsResource.class);
        this.resourceResolver = new YamlDocumentsResourceResolver(testClass);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object valueToInject(Type valueType, GivenYamlDocumentsResource resourceAnnotation) {
        SnakeYamlResourceParser parser = findSnakeYamlParser(resourceAnnotation);
        Class<?> elementType = ClassSupport.fromType(ClassSupport.elementType(valueType));

        try (ResourceAsReader resource = resourceResolver.resolve(resourceAnnotation)) {
            Iterable<Object> documents = parser.getYaml().loadAll(resource.reader());

            if (ClassSupport.isArray(valueType)) {
                return StreamSupport
                        .stream(documents.spliterator(), false)
                        .toArray(length -> (Object[]) Array.newInstance(elementType, length));
            } else if (ClassSupport.isCollection(valueType)) {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) valueType);
                documents.forEach(collection::add);
                return collection;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }

    @Override
    @Nullable
    protected String yamlParserName(GivenYamlDocumentsResource resourceAnnotation) {
        String yamlParserName = resourceAnnotation.yaml();
        if (StringUtils.isBlank(yamlParserName)) {
            yamlParserName = null;
        }

        return yamlParserName;
    }
}
