package io.hosuaby.inject.resources.junit.jupiter.json;

import io.hosuaby.inject.resources.commons.ClassSupport;
import io.hosuaby.inject.resources.commons.CollectionFactory;
import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.jupiter.GivenJsonLinesResource;
import io.hosuaby.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Stream;

import static io.hosuaby.inject.resources.commons.FieldAsserts.assertArrayOrCollection;
import static io.hosuaby.inject.resources.commons.FieldAsserts.assertNonPrivate;

public final class JsonLinesResourcesInjector extends AbstractJsonResourcesInjector<GivenJsonLinesResource> {
    private final JsonLinesResourceResolver resourceResolver;

    public JsonLinesResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenJsonLinesResource.class);
        this.resourceResolver = new JsonLinesResourceResolver(testClass);
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
    public Object valueToInject(Type valueType, GivenJsonLinesResource resourceAnnotation) {
        JsonParser jsonParser = findJsonParser(resourceAnnotation);

        Type elementType = ClassSupport.elementType(valueType);

        try (ResourceAsReader resource = resourceResolver.resolve(resourceAnnotation)) {
            Stream<Object> items = resource
                    .asLines()
                    .parseLines(line -> jsonParser.parse(line, elementType));

            if (ClassSupport.isArray(valueType)) {
                return items.toArray(length -> (Object[]) Array.newInstance((Class<?>) elementType, length));
            } else if (ClassSupport.isCollection(valueType))  {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) valueType);
                items.forEach(collection::add);
                return collection;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }

    @Override
    boolean isParsedWithJackson(GivenJsonLinesResource resourceAnnotation) {
        return StringUtils.isNotBlank(resourceAnnotation.jacksonMapper());
    }

    @Override
    String jacksonMapperName(GivenJsonLinesResource resourceAnnotation) {
        return resourceAnnotation.jacksonMapper();
    }

    @Override
    boolean isParsedWithGson(GivenJsonLinesResource resourceAnnotation) {
        return StringUtils.isNotBlank(resourceAnnotation.gson());
    }

    @Override
    String gsonName(GivenJsonLinesResource resourceAnnotation) {
        return resourceAnnotation.gson();
    }
}
