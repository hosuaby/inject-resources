package com.adelean.junit.jupiter.resources.common;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotatedFields;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotatedMethods;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.util.ReflectionUtils;

public abstract class ParserProvider<A extends Annotation> {
    protected final InjectionContext injectionContext;
    protected final Object testInstance;
    protected final Class<?> testClass;
    protected final Class<A> annotationType;

    public ParserProvider(
            InjectionContext injectionContext,
            Object testInstance,
            Class<?> testClass,
            Class<A> annotationType) {
        this.injectionContext = injectionContext;
        this.testInstance = testInstance;
        this.testClass = testClass;
        this.annotationType = annotationType;
    }

    public void provideFromStaticMethods() {
        findAnnotatedMethods(testClass, annotationType, ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .stream()
                .filter(ModifierSupport::isStatic)
                .forEach(method -> provideFromMethod(method, testClass));
    }

    public void provideFromInstanceMethods() {
        findAnnotatedMethods(testClass, annotationType, ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .stream()
                .filter(ModifierSupport::isNotStatic)
                .forEach(method -> provideFromMethod(method, testInstance));
    }

    public void provideFromStaticFields() {
        findAnnotatedFields(testClass, annotationType, ModifierSupport::isStatic)
                .forEach(this::provideFromField);
    }

    public void provideFromInstanceFields() {
        findAnnotatedFields(testClass, annotationType, ModifierSupport::isNotStatic)
                .forEach(this::provideFromField);
    }

    private void provideFromMethod(Method method, Object target) {
        MethodAsserts.assertReturnsSupportedType(method, annotationType);
        MethodAsserts.assertNonPrivate(method, annotationType);
        MethodAsserts.assertNoArguments(method, annotationType);
        A annotation = method.getAnnotation(annotationType);

        try {
            Object parser = makeAccessible(method).invoke(target);
            Class<?> parserType = parser.getClass();
            String parserName = parserNameFromMethod(method, annotation);
            injectionContext.defineBean(testClass, parserName, parserType, parser);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void provideFromField(Field field) {
        FieldAsserts.assertSupportedType(field, annotationType);
        FieldAsserts.assertNonPrivate(field, annotationType);
        A annotation = field.getAnnotation(annotationType);

        try {
            Object parser = makeAccessible(field).get(testInstance);
            Class<?> parserType = parser.getClass();
            String parserName = parserNameFromField(field, annotation);
            injectionContext.defineBean(testClass, parserName, parserType, parser);
        } catch (IllegalAccessException makeAccessibleException) {
            throw new RuntimeException(makeAccessibleException);
        }
    }

    protected String parserNameFromMethod(Method method, A annotation) {
        return method.getName();
    }

    protected String parserNameFromField(Field instanceField, A annotation) {
        return instanceField.getName();
    }
}
