package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.junit.jupiter.GivenBinaryResource;
import com.adelean.inject.resources.junit.jupiter.GivenJsonLinesResource;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.GivenPropertiesResource;
import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import com.adelean.inject.resources.junit.jupiter.GivenYamlResource;
import com.adelean.inject.resources.junit.jupiter.binary.BinaryResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.adelean.inject.resources.junit.jupiter.json.JsonLinesResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.json.JsonResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.properties.PropertiesResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.text.TextResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.yaml.YamlDocumentsResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.yaml.YamlResourcesInjector;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.adelean.inject.resources.commons.FieldAsserts.assertNonPrivate;
import static com.adelean.inject.resources.commons.FieldAsserts.assertSupportedType;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

public abstract class AbstractResourcesInjector<A extends Annotation> {
    public static final Map<
            Class<? extends Annotation>,
            Class<? extends AbstractResourcesInjector<? extends Annotation>>> INJECTORS = allInjectors();

    protected final InjectionContext injectionContext;
    protected final Object testInstance;
    protected final Class<?> testClass;
    protected final Class<A> annotationType;

    protected AbstractResourcesInjector(
            InjectionContext injectionContext,
            @Nullable
            Object testInstance,
            Class<?> testClass,
            Class<A> annotationType) {
        this.injectionContext = injectionContext;
        this.testInstance = testInstance;
        this.testClass = testClass;
        this.annotationType = annotationType;
    }

    public static <T extends Annotation> AbstractResourcesInjector<T> injectorFor(
            Class<T> annotationType,
            @Nullable
            Object testInstance,
            Class<?> testClass,
            InjectionContext injectionContext) {
        @SuppressWarnings("unchecked")
        Class<? extends AbstractResourcesInjector<T>> injectorClass = (Class<? extends AbstractResourcesInjector<T>>)
                INJECTORS.get(annotationType);

        try {
            return ReflectionUtils
                    .getDeclaredConstructor(injectorClass)
                    .newInstance(injectionContext, testInstance, testClass);
        } catch (Exception instantiationException) {
            throw new RuntimeException(instantiationException);
        }
    }

    public void injectField(Field field) {
        assertValidField(field);

        Type valueType = field.getGenericType();
        A resourceAnnotation = field.getAnnotation(annotationType);
        Object valueToInject = valueToInject(valueType, resourceAnnotation);

        try {
            makeAccessible(field)
                    .set(testInstance, valueToInject);
        } catch (IllegalAccessException makeAccessibleException) {
            throw new RuntimeException(makeAccessibleException);
        }
    }

    protected void assertValidField(Field field) {
        assertNonPrivate(field, annotationType);
        assertSupportedType(field, annotationType);
    }

    public void assertValidParameter(Parameter parameter) {
        assertSupportedType(parameter, annotationType);
    }

    abstract public Object valueToInject(Type valueType, A resourceAnnotation);

    public static Map<
            Class<? extends Annotation>,
            Class<? extends AbstractResourcesInjector<? extends Annotation>>> allInjectors() {
        Map<Class<? extends Annotation>, Class<? extends AbstractResourcesInjector<? extends Annotation>>> injectors =
                new HashMap<>();

        injectors.put(GivenBinaryResource.class, BinaryResourcesInjector.class);
        injectors.put(GivenTextResource.class, TextResourcesInjector.class);
        injectors.put(GivenPropertiesResource.class, PropertiesResourcesInjector.class);
        injectors.put(GivenJsonResource.class, JsonResourcesInjector.class);
        injectors.put(GivenJsonLinesResource.class, JsonLinesResourcesInjector.class);
        injectors.put(GivenYamlResource.class, YamlResourcesInjector.class);
        injectors.put(GivenYamlDocumentsResource.class, YamlDocumentsResourcesInjector.class);

        return Collections.unmodifiableMap(injectors);
    }
}
