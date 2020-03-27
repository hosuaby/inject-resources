package com.adelean.junit.jupiter.resources.core;

import com.adelean.junit.jupiter.resources.TestWithResourcesExtension;
import com.adelean.junit.jupiter.resources.core.cdi.InjectionContext;
import com.adelean.junit.jupiter.resources.core.helpers.Annotations;
import com.adelean.junit.jupiter.resources.core.helpers.ClassSupport;
import com.adelean.junit.jupiter.resources.core.helpers.FieldAsserts;
import com.adelean.junit.jupiter.resources.core.helpers.MethodAsserts;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

public abstract class AbstractParserProvider<
        A extends Annotation, R, P extends ResourceParser<? extends Parsable<?>, ?>> {
    public static final Map<
            Class<? extends Annotation>,
            Class<? extends AbstractParserProvider<?, ?, ?>>> PARSER_PROVIDERS = allParserProviders();

    protected final InjectionContext injectionContext;
    protected final Object testInstance;
    protected final Class<?> testClass;
    protected final Class<A> annotationType;

    public AbstractParserProvider(
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

    public static <T extends Annotation> AbstractParserProvider<T, ?, ?> providerFor(
            Class<T> annotationType,
            @Nullable
            Object testInstance,
            Class<?> testClass,
            InjectionContext injectionContext) {
        @SuppressWarnings("unchecked")
        Class<? extends AbstractParserProvider<T, ?, ?>> providerClass =
                (Class<? extends AbstractParserProvider<T, ?, ?>>) PARSER_PROVIDERS.get(annotationType);

        try {
            return ReflectionUtils
                    .getDeclaredConstructor(providerClass)
                    .newInstance(injectionContext, testInstance, testClass);
        } catch (Exception instantiationException) {
            throw new RuntimeException(instantiationException);
        }
    }

    public void provideFromMethod(Method method, Object target) {
        MethodAsserts.assertReturnsSupportedType(method, annotationType);
        MethodAsserts.assertNonPrivate(method, annotationType);
        MethodAsserts.assertNoArguments(method, annotationType);
        A parserAnnotation = method.getAnnotation(annotationType);

        try {
            @SuppressWarnings("unchecked")
            R parser = (R) makeAccessible(method).invoke(target);

            String parserName = parserNameFromMethod(method, parserAnnotation);
            P parserBean = createParser(parserAnnotation, parser);
            injectionContext.defineBean(testClass, parserName, parserBean.getClass(), parserBean);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void provideFromField(Field field) {
        FieldAsserts.assertSupportedType(field, annotationType);
        FieldAsserts.assertNonPrivate(field, annotationType);
        A parserAnnotation = field.getAnnotation(annotationType);

        @SuppressWarnings("unchecked")
        R parser = (R) ReflectionSupport
                .tryToReadFieldValue(field, testInstance)
                .getOrThrow(RuntimeException::new);

        String parserName = parserNameFromField(field, parserAnnotation);
        P parserBean = createParser(parserAnnotation, parser);
        injectionContext.defineBean(testClass, parserName, parserBean.getClass(), parserBean);
    }

    protected String parserNameFromMethod(Method method, A annotation) {
        String parserName = Annotations.getName(annotation);
        return parserName != null ? parserName : method.getName();
    }

    protected String parserNameFromField(Field field, A annotation) {
        String parserName = Annotations.getName(annotation);
        return parserName != null ? parserName : field.getName();
    }

    protected abstract P createParser(A parserAnnotation, R parser);

    @SuppressWarnings("unchecked")
    public static Map<
            Class<? extends Annotation>,
            Class<? extends AbstractParserProvider<?, ?, ?>>> allParserProviders() {
        return ReflectionSupport
                .findAllClassesInPackage(
                        TestWithResourcesExtension.class.getPackage().getName(),
                        clazz -> ClassSupport.isSubclass(clazz, AbstractParserProvider.class),
                        any -> true)
                .stream()
                .filter(ModifierSupport::isPublic)
                .map(clazz -> (Class<? extends AbstractParserProvider<?, ?, ?>>) clazz)
                .filter(AbstractParserProvider::isLoadable)
                .collect(toMap(
                        AbstractParserProvider::extractParserAnnotationArg,
                        providerClass -> providerClass));
    }

    static boolean isLoadable(Class<? extends AbstractParserProvider<?, ?, ?>> providerClass) {
        try {
            providerClass.getGenericSuperclass();
            return true;
        } catch (TypeNotPresentException missingDependencyException) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    static Class<? extends Annotation> extractParserAnnotationArg(
            Class<? extends AbstractParserProvider<?, ?, ?>> providerClass) {
        return (Class<? extends Annotation>)
                ((ParameterizedType) providerClass.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
