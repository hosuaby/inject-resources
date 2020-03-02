package com.adelean.junit.jupiter.resources;

import com.adelean.junit.jupiter.resources.common.InjectionContext;
import com.adelean.junit.jupiter.resources.common.ResourcesInjector;
import com.adelean.junit.jupiter.resources.json.JacksonJsonResourcesInjector;
import com.adelean.junit.jupiter.resources.json.JacksonMapperProvider;
import com.adelean.junit.jupiter.resources.text.TextResourcesInjector;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import static com.adelean.junit.jupiter.resources.common.FieldAsserts.assertNotConstructor;
import static com.adelean.junit.jupiter.resources.common.FieldAsserts.assertNotStaticMethod;

public class TestResourcesExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

    /**
     * Performs field injection for non-private, {@code static} fields (i.e., class fields) that are annotated with
     * one of {@code Given*Resource} annotation with content of resource file.
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        injectStaticFields(context, context.getRequiredTestClass());
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
                .forEach(instance -> injectInstanceFields(context, instance));
    }

    /**
     * Determines if the {@link Parameter} in the supplied {@link ParameterContext} is annotated with one of
     * {@code Given*Resource} annotation.
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return ResourcesInjector
                .getResourceAnnotation(parameterContext)
                .map(Annotation::annotationType)
                .map(annotationType -> {
                    assertNotConstructor(parameterContext.getDeclaringExecutable(), annotationType);
                    assertNotStaticMethod(parameterContext.getDeclaringExecutable(), annotationType);
                    return annotationType;
                })
                .isPresent();
    }

    /**
     * Injects the resource into {@link Parameter} in the supplied {@link ParameterContext}.
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        ResourcesInjector<Annotation> injector = (ResourcesInjector<Annotation>)
                ResourcesInjector.injectorFor(parameterContext, extensionContext);
        injector.isParameterSupported(parameterContext);
        Annotation parameterAnnotation = injector.getParameterAnnotation(parameterContext);
        Type parameterType = parameterContext.getParameter().getParameterizedType();
        return injector.valueToInject(parameterType, parameterAnnotation);
    }

    private void injectStaticFields(ExtensionContext context, Class<?> testClass) {
        InjectionContext injectionContext = new InjectionContext(context);

        JacksonMapperProvider jacksonMapperProvider = new JacksonMapperProvider(injectionContext, null, testClass);
        jacksonMapperProvider.provideFromStaticMethods();
        jacksonMapperProvider.provideFromStaticFields();

        new TextResourcesInjector(context, null, testClass)
                .injectStaticFields();
    }

    private void injectInstanceFields(ExtensionContext context, Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        InjectionContext injectionContext = new InjectionContext(context);

        JacksonMapperProvider jacksonMapperProvider = new JacksonMapperProvider(
                injectionContext, testInstance, testClass);
        jacksonMapperProvider.provideFromInstanceMethods();
        jacksonMapperProvider.provideFromInstanceFields();

        new TextResourcesInjector(context, testInstance, testClass)
                .injectInstanceFields();
        new JacksonJsonResourcesInjector(context, testInstance, testClass)
                .injectInstanceFields();
    }
}
