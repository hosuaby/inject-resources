package com.adelean.inject.resources.junit.jupiter.json;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.GivenJsonLinesResource;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.adelean.inject.resources.junit.jupiter.core.helpers.CollectionFactory;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Stream;

import static com.adelean.inject.resources.core.helpers.StringUtils.ucfirst;
import static com.adelean.inject.resources.junit.jupiter.core.helpers.FieldAsserts.assertNonPrivate;

public final class JacksonJsonLinesResourcesInjector extends AbstractJacksonResourcesInjector<GivenJsonLinesResource> {
    private static final String ERR_TARGET_NOT_ARRAY_OR_COLLECTION =
            "@%s cannot be resolved on %s of type %s. %s must be array or collection.";

    private final JsonLinesResourceResolver resourceResolver;

    public JacksonJsonLinesResourcesInjector(
            InjectionContext injectionContext,
            @Nullable
            Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, GivenJsonLinesResource.class);
        this.resourceResolver = new JsonLinesResourceResolver(testClass);
    }

    @Override
    protected void assertValidField(Field field) {
        assertNonPrivate(field, annotationType);
        assertArrayOrCollection("field", field.getType());
    }

    @Override
    public void assertValidParameter(Parameter parameter) {
        assertArrayOrCollection("parameter", parameter.getType());
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object valueToInject(Type valueType, GivenJsonLinesResource resourceAnnotation) {
        JsonParser jsonParser = findJsonParser(resourceAnnotation);

        Type elementType = elementType(valueType);

        try (ResourceAsReader resource = resourceResolver.resolve(resourceAnnotation)) {
            Stream<Object> items = resource
                    .asLines()
                    .parseLines(line -> jsonParser.parse(line, elementType));

            if (isArray(valueType)) {
                return items.toArray(length -> (Object[]) Array.newInstance((Class<?>) elementType, length));
            } else if (valueType instanceof ParameterizedType)  {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) valueType);
                items.forEach(collection::add);
                return collection;
            }

            return null;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
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

    @Nullable
    static Type elementType(Type valueType) {
        Type elementType = null;

        if (isArray(valueType)) {
            Class<?> arrayType = (Class<?>) valueType;
            elementType = arrayType.getComponentType();
        } else if (valueType instanceof ParameterizedType) {
            ParameterizedType collectionType = (ParameterizedType) valueType;
            elementType = collectionType.getActualTypeArguments()[0];
        }

        return elementType;
    }

    static boolean isArray(Type valueType) {
        return valueType instanceof Class && ((Class<?>) valueType).isArray();
    }

    static void assertArrayOrCollection(String target, Class<?> targetType) {
        boolean valid = targetType.isArray() || Collection.class.isAssignableFrom(targetType);

        if (!valid) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_TARGET_NOT_ARRAY_OR_COLLECTION,
                    GivenJsonLinesResource.class.getSimpleName(),
                    target,
                    targetType.getName(),
                    ucfirst(target)));
        }
    }
}
