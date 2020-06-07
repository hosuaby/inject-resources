package com.adelean.inject.resources.junit.jupiter;

import com.adelean.inject.resources.junit.jupiter.core.AbstractParserProvider;
import com.adelean.inject.resources.junit.jupiter.core.AbstractResourcesInjector;
import com.adelean.inject.resources.junit.jupiter.core.TestContext;
import com.adelean.inject.resources.junit.jupiter.core.TestsAdviceProcessor;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.HierarchyTraversalMode;
import org.junit.platform.commons.support.ReflectionSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.adelean.inject.resources.junit.jupiter.core.helpers.Annotations.PARSER_ANNOTATIONS;
import static com.adelean.inject.resources.junit.jupiter.core.helpers.Annotations.RESOURCE_ANNOTATIONS;
import static com.adelean.inject.resources.junit.jupiter.core.helpers.FieldAsserts.assertNotConstructor;
import static com.adelean.inject.resources.junit.jupiter.core.helpers.FieldAsserts.assertNotStaticMethod;
import static java.util.stream.Collectors.toList;

public class TestWithResourcesExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

    /**
     * Performs field injection for non-private, {@code static} fields (i.e., class fields) that are annotated with
     * one of {@code Given*Resource} annotation with content of resource file.
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        TestContext testContext = TestContext.ofClass(context.getRequiredTestClass(), context);
        inject(testContext);

        if (!isNestedTestClassContext(context)) {
            TestsAdviceProcessor
                    .findAdviceClass()
                    .map(ReflectionSupport::newInstance)
                    .ifPresent(adviceInstance -> provideParsersFromAdvice(adviceInstance, context));
        }
    }

    static boolean isNestedTestClassContext(ExtensionContext context) {
        return context.getParent().flatMap(ExtensionContext::getElement).isPresent();
    }

    /**
     * Performs field injection for non-private, non-static fields (i.e., instance fields) that are annotated with
     * one of {@code Given*Resource} annotation with content of resource file.
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        context
                .getRequiredTestInstances()
                .getAllInstances()
                .stream()
                .map(testInstance -> TestContext.ofInstance(testInstance, context))
                .forEach(this::inject);
    }

    /**
     * Determines if the {@link Parameter} in the supplied {@link ParameterContext} is annotated with one of
     * {@code Given*Resource} annotation.
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Annotation resourceAnnotation = getResourceAnnotation(parameterContext).orElse(null);

        if (resourceAnnotation != null) {
            assertParameter(resourceAnnotation.annotationType(), parameterContext, extensionContext);
            return true;
        }

        return false;
    }

    static void assertParameter(
            Class<? extends Annotation> annotationType,
            ParameterContext parameterContext,
            ExtensionContext extensionContext) {
        assertNotConstructor(parameterContext.getDeclaringExecutable(), annotationType);
        assertNotStaticMethod(parameterContext.getDeclaringExecutable(), annotationType);

        Object testInstance = parameterContext.getTarget().get();
        TestContext testContext = TestContext.ofInstance(testInstance, extensionContext);

        @SuppressWarnings("unchecked")
        AbstractResourcesInjector<Annotation> injector = AbstractResourcesInjector.injectorFor(
                (Class<Annotation>) annotationType,
                testInstance,
                testContext.getTestClass(),
                testContext.getInjectionContext());

        injector.assertValidParameter(parameterContext.getParameter());
    }

    /**
     * Injects the resource into {@link Parameter} in the supplied {@link ParameterContext}.
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        /* Method guaranteed to be non-static so call to get() will succeed */
        Object testInstance = parameterContext.getTarget().get();
        Annotation resourceAnnotation = getResourceAnnotation(parameterContext).get();

        TestContext testContext = TestContext.ofInstance(testInstance, extensionContext);

        @SuppressWarnings("unchecked")
        AbstractResourcesInjector<Annotation> injector = AbstractResourcesInjector.injectorFor(
                (Class<Annotation>) resourceAnnotation.annotationType(),
                testInstance,
                testContext.getTestClass(),
                testContext.getInjectionContext());

        Type parameterType = parameterContext.getParameter().getParameterizedType();
        return injector.valueToInject(parameterType, resourceAnnotation);
    }

    static Optional<? extends Annotation> getResourceAnnotation(ParameterContext parameterContext) {
        return RESOURCE_ANNOTATIONS
                .stream()
                .filter(parameterContext::isAnnotated)
                .findAny()
                .flatMap(parameterContext::findAnnotation);
    }

    private void inject(TestContext testContext) {
        provideParsers(testContext);
        injectFields(testContext);
    }

    private void provideParsers(TestContext testContext) {
        for (Class<? extends Annotation> annotationType : PARSER_ANNOTATIONS) {
            List<Field> providingFields = AnnotationSupport.findAnnotatedFields(
                    testContext.getTestClass(),
                    annotationType,
                    testContext.memberSelector(),
                    HierarchyTraversalMode.TOP_DOWN);

            List<Method> providingMethods =
                    AnnotationSupport.findAnnotatedMethods(testContext.getTestClass(), annotationType, HierarchyTraversalMode.TOP_DOWN)
                            .stream()
                            .filter(testContext.memberSelector())
                            .collect(toList());

            if (!providingFields.isEmpty() || !providingMethods.isEmpty()) {
                AbstractParserProvider<? extends Annotation, ?, ?> parserProvider = AbstractParserProvider
                        .providerFor(
                                annotationType,
                                testContext.getTestInstance(),
                                testContext.getTestClass(),
                                testContext.getInjectionContext());
                providingFields.forEach(parserProvider::provideFromField);
                providingMethods.forEach(method -> parserProvider.provideFromMethod(
                        method, testContext.getTestInstance()));
            }
        }
    }

    private void provideParsersFromAdvice(Object adviceInstance, ExtensionContext context) {
        InjectionContext injectionContext = new InjectionContext(context);

        for (Class<? extends Annotation> annotationType : PARSER_ANNOTATIONS) {
            List<Field> providingFields = AnnotationSupport.findAnnotatedFields(
                    adviceInstance.getClass(),
                    annotationType,
                    anyField -> true,
                    HierarchyTraversalMode.TOP_DOWN);

            List<Method> providingMethods = new ArrayList<>(
                    AnnotationSupport.findAnnotatedMethods(
                            adviceInstance.getClass(), annotationType, HierarchyTraversalMode.TOP_DOWN));

            if (!providingFields.isEmpty() || !providingMethods.isEmpty()) {
                AbstractParserProvider<? extends Annotation, ?, ?> parserProvider = AbstractParserProvider
                        .providerFor(
                                annotationType,
                                adviceInstance,
                                Object.class,
                                injectionContext);
                providingFields.forEach(parserProvider::provideFromField);
                providingMethods.forEach(method -> parserProvider.provideFromMethod(
                        method, adviceInstance));
            }
        }
    }

    private void injectFields(TestContext testContext) {
        for (Class<? extends Annotation> annotationType : RESOURCE_ANNOTATIONS) {
            List<Field> fieldsForInjection = AnnotationSupport.findAnnotatedFields(
                    testContext.getTestClass(),
                    annotationType,
                    testContext.memberSelector(),
                    HierarchyTraversalMode.TOP_DOWN);

            if (!fieldsForInjection.isEmpty()) {
                AbstractResourcesInjector<? extends Annotation> resourcesInjector = AbstractResourcesInjector
                        .injectorFor(
                                annotationType,
                                testContext.getTestInstance(),
                                testContext.getTestClass(),
                                testContext.getInjectionContext());
                fieldsForInjection.forEach(resourcesInjector::injectField);
            }
        }
    }
}
