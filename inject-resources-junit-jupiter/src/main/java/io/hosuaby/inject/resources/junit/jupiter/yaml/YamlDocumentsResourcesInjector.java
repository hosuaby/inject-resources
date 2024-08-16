package io.hosuaby.inject.resources.junit.jupiter.yaml;

import static io.hosuaby.inject.resources.commons.FieldAsserts.assertArrayOrCollection;
import static io.hosuaby.inject.resources.commons.FieldAsserts.assertNonPrivate;

import io.hosuaby.inject.resources.commons.ClassSupport;
import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.core.helpers.StringUtils;
import io.hosuaby.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import io.hosuaby.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import io.hosuaby.inject.resources.commons.CollectionFactory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
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
    protected void assertValidField(Field field) {
        assertNonPrivate(field, annotationType);
        assertArrayOrCollection("field", field.getType(), annotationType);
    }

    @Override
    public void assertValidParameter(Parameter parameter) {
        assertArrayOrCollection("parameter", parameter.getType(), annotationType);
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
        return StringUtils.blankToNull(resourceAnnotation.yaml());
    }
}
