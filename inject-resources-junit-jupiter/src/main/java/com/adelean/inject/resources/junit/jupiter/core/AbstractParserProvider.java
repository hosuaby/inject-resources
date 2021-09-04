package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.commons.FieldAsserts;
import com.adelean.inject.resources.commons.MethodAsserts;
import com.adelean.inject.resources.core.Parsable;
import com.adelean.inject.resources.junit.jupiter.WithGson;
import com.adelean.inject.resources.junit.jupiter.WithJacksonMapper;
import com.adelean.inject.resources.junit.jupiter.WithSnakeYaml;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.adelean.inject.resources.junit.jupiter.json.GsonProvider;
import com.adelean.inject.resources.junit.jupiter.json.JacksonMapperProvider;
import com.adelean.inject.resources.junit.jupiter.yaml.SnakeYamlProvider;
import org.jetbrains.annotations.Nullable;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        String parserName = AnnotationSupport.getName(annotation);
        return parserName != null ? parserName : method.getName();
    }

    protected String parserNameFromField(Field field, A annotation) {
        String parserName = AnnotationSupport.getName(annotation);
        return parserName != null ? parserName : field.getName();
    }

    protected abstract P createParser(A parserAnnotation, R parser);

    public static Map<
            Class<? extends Annotation>,
            Class<? extends AbstractParserProvider<?, ?, ?>>> allParserProviders() {
        Map<Class<? extends Annotation>, Class<? extends AbstractParserProvider<?, ?, ?>>> providers = new HashMap<>();

        providers.put(WithJacksonMapper.class, JacksonMapperProvider.class);
        providers.put(WithGson.class, GsonProvider.class);
        providers.put(WithSnakeYaml.class, SnakeYamlProvider.class);

        return Collections.unmodifiableMap(providers);
    }
}
