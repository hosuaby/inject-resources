package com.adelean.junit.jupiter.resources.common;

import static com.adelean.junit.jupiter.resources.common.FieldAsserts.assertNonPrivate;
import static com.adelean.junit.jupiter.resources.common.FieldAsserts.assertSupportedType;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotatedFields;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

import com.adelean.junit.jupiter.resources.TestResourcesExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ResourcesInjector<A extends Annotation> {
    private static final Pattern RESOURCE_ANNOTATION_NAME_PATTERN = Pattern.compile("Given\\w+Resource");
    private static final List<Class<? extends Annotation>> RESOURCE_ANNOTATIONS = getResourceAnnotations();
    private static final Map<
            Class<? extends Annotation>,
            Class<? extends ResourcesInjector<? extends Annotation>>> INJECTORS = injectors();

    protected final ExtensionContext context;
    protected final Object testInstance;
    protected final Class<?> testClass;
    protected final Class<A> annotationType;
    protected final ResourceLoader resourceLoader;

    public ResourcesInjector(
            ExtensionContext context,
            Object testInstance,
            Class<?> testClass,
            Class<A> annotationType) {
        this.context = context;
        this.testInstance = testInstance;
        this.testClass = testClass;
        this.annotationType = annotationType;
        this.resourceLoader = new ResourceLoader(testClass);
    }

    public void injectStaticFields() {
        findAnnotatedFields(testClass, annotationType, ModifierSupport::isStatic)
                .forEach(this::injectField);
    }

    public void injectInstanceFields() {
        findAnnotatedFields(testClass, annotationType, ModifierSupport::isNotStatic)
                .forEach(this::injectField);
    }

    public static ResourcesInjector<? extends Annotation> injectorFor(
            ParameterContext parameterContext,
            ExtensionContext extensionContext) {

        /* Method guaranteed to be non-static so call to get() will succeed */
        Object testInstance = parameterContext.getTarget().get();
        Class<?> testClass = testInstance.getClass();
        Annotation resourceAnnotation = getResourceAnnotation(parameterContext).get();  // guaranteed to succeed
        Class<? extends Annotation> annotationType = resourceAnnotation.annotationType();
        Class<? extends ResourcesInjector<? extends Annotation>> injectorClass = INJECTORS
                .get(annotationType);

        try {
            return ReflectionUtils
                    .getDeclaredConstructor(injectorClass)
                    .newInstance(extensionContext, testInstance, testClass);
        } catch (Exception instantiationException) {
            throw new RuntimeException(instantiationException);
        }
    }

    // TODO: rename to assertParameterSupported
    public boolean isParameterSupported(ParameterContext parameterContext) {
        A resourceAnnotation = parameterContext.findAnnotation(annotationType).orElse(null);

        if (resourceAnnotation == null) {
            return false;
        }

        assertSupportedType(parameterContext.getParameter(), annotationType);
        return true;
    }

    private void injectField(Field field) {
        assertSupportedType(field, annotationType);
        assertNonPrivate(field, annotationType);

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

    public A getParameterAnnotation(ParameterContext parameterContext) {
        return parameterContext.getParameter().getAnnotation(annotationType);
    }

    abstract public Object valueToInject(Type valueType, A resourceAnnotation);

    public static Optional<? extends Annotation> getResourceAnnotation(ParameterContext parameterContext) {
        return RESOURCE_ANNOTATIONS
                .stream()
                .filter(parameterContext::isAnnotated)
                .findAny()
                .flatMap(parameterContext::findAnnotation);
    }

    @SuppressWarnings("unchecked")
    static List<Class<? extends Annotation>> getResourceAnnotations() {
        return ReflectionSupport
                .findAllClassesInPackage(
                        TestResourcesExtension.class.getPackage().getName(),
                        Class::isAnnotation,
                        RESOURCE_ANNOTATION_NAME_PATTERN.asPredicate())
                .stream()
                .map(clazz -> (Class<? extends Annotation>) clazz)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    static Map<Class<? extends Annotation>, Class<? extends ResourcesInjector<? extends Annotation>>> injectors() {
        Map<Class<? extends Annotation>, Class<? extends ResourcesInjector<? extends Annotation>>> injectors =
                new HashMap<>();

        ReflectionSupport
                .findAllClassesInPackage(
                        TestResourcesExtension.class.getPackage().getName(),
                        ResourcesInjector.class::isAssignableFrom,
                        any -> true)
                .stream()
                .filter(clazz -> clazz != ResourcesInjector.class)
                .forEach(injectorClass ->  {
                    Class<? extends Annotation> annotationType = (Class<? extends Annotation>)
                            ((ParameterizedType) injectorClass.getGenericSuperclass()).getActualTypeArguments()[0];
                    injectors.put(
                            annotationType,
                            (Class<? extends ResourcesInjector<? extends Annotation>>) injectorClass);
                });

        return injectors;
    }
}
