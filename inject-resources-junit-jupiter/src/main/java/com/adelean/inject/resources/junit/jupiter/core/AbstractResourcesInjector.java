package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.commons.ClassSupport;
import com.adelean.inject.resources.junit.jupiter.TestWithResourcesExtension;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import static com.adelean.inject.resources.junit.jupiter.core.helpers.FieldAsserts.assertNonPrivate;
import static com.adelean.inject.resources.junit.jupiter.core.helpers.FieldAsserts.assertSupportedType;
import static java.util.stream.Collectors.toMap;
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

    @SuppressWarnings("unchecked")
    public static Map<
            Class<? extends Annotation>,
            Class<? extends AbstractResourcesInjector<? extends Annotation>>> allInjectors() {
        return ReflectionSupport
                .findAllClassesInPackage(
                        TestWithResourcesExtension.class.getPackage().getName(),
                        clazz -> ClassSupport.isSubclass(clazz, AbstractResourcesInjector.class),
                        any -> true)
                .stream()
                .filter(ModifierSupport::isPublic)
                .map(clazz -> (Class<? extends AbstractResourcesInjector<? extends Annotation>>) clazz)
                .collect(toMap(
                        injectorClass -> (Class<? extends Annotation>)
                                ((ParameterizedType) injectorClass.getGenericSuperclass()).getActualTypeArguments()[0],
                        injectorClass -> injectorClass));
    }
}
