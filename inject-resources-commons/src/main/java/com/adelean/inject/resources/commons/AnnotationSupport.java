package com.adelean.inject.resources.commons;

import com.adelean.inject.resources.annotations.Extends;
import com.adelean.inject.resources.annotations.Named;
import com.adelean.inject.resources.annotations.Parser;
import com.adelean.inject.resources.annotations.Resource;
import com.adelean.inject.resources.annotations.WithPath;
import com.adelean.inject.resources.core.helpers.StringUtils;
import org.reflections8.ReflectionUtils;
import org.reflections8.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.adelean.inject.resources.commons.Errors.internalError;
import static java.util.stream.Collectors.toList;
import static org.reflections8.ReflectionUtils.withName;
import static org.reflections8.ReflectionUtils.withReturnType;

public final class AnnotationSupport {
    private static final String ERR_ANNOTATION_NOT_EXTENDS = "@%s missing @Extends(%s.class)";
    private static final String ERR_ANNOTATION_MISSING_METHODS = "@%s missing methods: %s";

    private static final String[] FROM_METHOD_NAMES = { "from", "value" };
    private static final String[] NAME_METHOD_NAMES = { "name", "value" };

    private AnnotationSupport() {
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

    @SuppressWarnings("unchecked")
    static String getFirst(Annotation resourceAnnotation, String... methodNames) {
        Class<? extends Annotation> annotationType = resourceAnnotation.annotationType();

        return Stream
                .of(methodNames)
                .map(methodName -> ReflectionUtils
                        .getMethods(annotationType, withName(methodName))
                        .stream()
                        .map(method -> valueFromMethod(method, resourceAnnotation))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findAny())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElse(null);
    }

    static Optional<String> valueFromMethod(Method method, Annotation resourceAnnotation) {
        try {
            Object value = method.invoke(resourceAnnotation);
            String strValue = (String) value;
            return Optional.ofNullable(StringUtils.blankToNull(strValue));
        } catch (IllegalAccessException | InvocationTargetException methodInvocationException) {
            throw new RuntimeException(methodInvocationException);
        }
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
        List<Class<? extends Annotation>> extendedAnnotationTypes = AnnotationUtils
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
        Extends extendsAnnotation = AnnotationUtils
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

    @SuppressWarnings("unchecked")
    static boolean isHasMethod(Class<?> clazz, Method method) {
        return !ReflectionUtils
                .getMethods(clazz, withName(method.getName()), withReturnType(method.getReturnType()))
                .isEmpty();
    }

    public static Collection<Class<? extends Annotation>> allResourceAnnotations(Class<?> baseClass) {
        return new Reflections(baseClass.getPackage().getName(), baseClass.getClassLoader())
                .getSubTypesOf(Annotation.class)
                .stream()
                .filter(clazz -> AnnotationUtils
                        .findAnnotation(clazz, Resource.class)
                        .isPresent())
                .filter(clazz -> Modifier.isPublic(clazz.getModifiers()))
                .collect(toList());
    }

    public static Collection<Class<? extends Annotation>> allParserAnnotations(Class<?> baseClass) {
        return new Reflections(baseClass.getPackage().getName(), baseClass.getClassLoader())
                .getSubTypesOf(Annotation.class)
                .stream()
                .filter(clazz -> AnnotationUtils
                        .findAnnotation(clazz, Parser.class)
                        .isPresent())
                .filter(clazz -> Modifier.isPublic(clazz.getModifiers()))
                .collect(toList());
    }
}
