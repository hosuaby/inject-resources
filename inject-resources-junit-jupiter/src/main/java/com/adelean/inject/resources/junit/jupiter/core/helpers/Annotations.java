package com.adelean.inject.resources.junit.jupiter.core.helpers;

import com.adelean.inject.resources.junit.jupiter.TestWithResourcesExtension;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Extends;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Named;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Parser;
import com.adelean.inject.resources.junit.jupiter.core.annotations.Resource;
import com.adelean.inject.resources.junit.jupiter.core.annotations.WithPath;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.adelean.inject.resources.junit.jupiter.core.helpers.Errors.internalError;
import static java.util.stream.Collectors.toList;

public final class Annotations {
    public static final Collection<Class<? extends Annotation>> RESOURCE_ANNOTATIONS = allResourceAnnotations();
    public static final Collection<Class<? extends Annotation>> PARSER_ANNOTATIONS = allParserAnnotations();

    private static final String ERR_ANNOTATION_NOT_EXTENDS = "@%s missing @Extends(%s.class)";
    private static final String ERR_ANNOTATION_MISSING_METHODS = "@%s missing methods: %s";

    private static final String[] FROM_METHOD_NAMES = { "from", "value" };
    private static final String[] NAME_METHOD_NAMES = { "name", "value" };

    private Annotations() {
    }

    public static String getFrom(Annotation resourceAnnotation) {
        assertInheritProperly(resourceAnnotation.annotationType());
        assertAnnotationExtends(resourceAnnotation.annotationType(), WithPath.class);
        return getFirst(resourceAnnotation, FROM_METHOD_NAMES);
    }

    public static String getName(Annotation parserAnnotation) {
        assertInheritProperly(parserAnnotation.annotationType());
        assertAnnotationExtends(parserAnnotation.annotationType(), Named.class);
        return getFirst(parserAnnotation, NAME_METHOD_NAMES);
    }

    static String getFirst(Annotation resourceAnnotation, String... methodNames) {
        Class<? extends Annotation> annotationType = resourceAnnotation.annotationType();

        return Stream
                .of(methodNames)
                .map(methodName -> ReflectionSupport.findMethod(annotationType, methodName))
                .map(foundMethod -> foundMethod.flatMap(method -> valueFromMethod(method, resourceAnnotation)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElse(null);
    }

    static Optional<String> valueFromMethod(Method method, Annotation resourceAnnotation) {
        Object value = ReflectionSupport.invokeMethod(method, resourceAnnotation);
        String strValue = (String) value;
        return StringUtils.isNotBlank(strValue) ? Optional.of(strValue) : Optional.empty();
    }

    static void assertAnnotationExtends(
            Class<? extends Annotation> annotationType,
            Class<? extends Annotation> annotationSuperType) {
        if (!isAnnotationExtends(annotationType, annotationSuperType)) {
            throw new RuntimeException(internalError(String.format(
                        ERR_ANNOTATION_NOT_EXTENDS,
                        annotationType.getSimpleName(),
                        annotationSuperType.getSimpleName())));
        }
    }

    static boolean isAnnotationExtends(
            Class<? extends Annotation> annotationType,
            Class<? extends Annotation> annotationSuperType) {
        List<Class<? extends Annotation>> extendedAnnotationTypes = AnnotationSupport
                .findAnnotation(annotationType, Extends.class)
                .map(Extends::value)
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList);

        if (extendedAnnotationTypes.contains(annotationSuperType)) {
            return true;
        }

        return extendedAnnotationTypes
                .stream()
                .anyMatch(extendedAnnotationType -> isAnnotationExtends(extendedAnnotationType, annotationSuperType));
    }

    public static void assertInheritProperly(Class<? extends Annotation> annotationType) {
        Extends extendsAnnotation = AnnotationSupport
                .findAnnotation(annotationType, Extends.class)
                .orElse(null);

        if (extendsAnnotation == null) {
            return;
        }

        Class<? extends Annotation>[] extended = extendsAnnotation.value();
        Set<Method> requiredMethods = Stream
                .of(extended)
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .collect(Collectors.toSet());

        Set<Method> missingMethods = requiredMethods
                .stream()
                .filter(requiredMethod -> !isHasMethod(annotationType, requiredMethod))
                .collect(Collectors.toSet());

        if (!missingMethods.isEmpty()) {
            String missing = missingMethods
                    .stream()
                    .map(missingMethod -> String.format(
                            "[%s %s()]", missingMethod.getReturnType().getSimpleName(), missingMethod.getName()))
                    .collect(Collectors.joining(", "));

            throw new RuntimeException(internalError(String.format(
                    ERR_ANNOTATION_MISSING_METHODS, annotationType.getSimpleName(), missing)));
        }
    }

    static boolean isHasMethod(Class<?> clazz, Method method) {
        return ReflectionSupport
                .findMethod(clazz, method.getName())
                .filter(foundMethod -> foundMethod.getReturnType() == method.getReturnType())
                .isPresent();
    }

    @SuppressWarnings("unchecked")
    public static Collection<Class<? extends Annotation>> allResourceAnnotations() {
        return ReflectionSupport
                .findAllClassesInPackage(
                        TestWithResourcesExtension.class.getPackage().getName(),
                        Annotations::isResourceAnnotation,
                        any -> true)
                .stream()
                .map(clazz -> (Class<? extends Annotation>) clazz)
                .collect(toList());
    }

    static boolean isResourceAnnotation(Class<?> clazz) {
        return clazz.isAnnotation()
                && ModifierSupport.isPublic(clazz)
                && AnnotationSupport.isAnnotated(clazz, Resource.class);
    }

    @SuppressWarnings("unchecked")
    public static Collection<Class<? extends Annotation>> allParserAnnotations() {
        return ReflectionSupport
                .findAllClassesInPackage(
                        TestWithResourcesExtension.class.getPackage().getName(),
                        Annotations::isParserAnnotation,
                        any -> true)
                .stream()
                .map(clazz -> (Class<? extends Annotation>) clazz)
                .collect(toList());
    }

    static boolean isParserAnnotation(Class<?> clazz) {
        return clazz.isAnnotation()
                && ModifierSupport.isPublic(clazz)
                && AnnotationSupport.isAnnotated(clazz, Parser.class);
    }
}
