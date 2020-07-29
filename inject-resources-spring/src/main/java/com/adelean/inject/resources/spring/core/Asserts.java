package com.adelean.inject.resources.spring.core;

import static com.adelean.inject.resources.commons.ClassSupport.isArray;
import static com.adelean.inject.resources.commons.ClassSupport.isCollection;
import static com.adelean.inject.resources.spring.core.Annotations.annotationsToString;
import static com.adelean.inject.resources.spring.core.Annotations.invalidAnnotations;

import com.adelean.inject.resources.core.helpers.StringUtils;
import org.springframework.beans.factory.BeanCreationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public final class Asserts {
    private static final String ERR_TYPE_NOT_ARRAY_OR_COLLECTION =
            "%s annotated with @%s must be array or collection, but was %s.";
    private static final String ERR_INVALID_ANNOTATIONS =
            "%s '%s' annotated with @%s has other invalid annotations:\n%s";

    private Asserts() {
    }

    public static Type assertArrayOrCollection(
            String target,
            Type targetType,
            Class<? extends Annotation> resourceAnnotationType) {
        boolean valid = isArray(targetType) || isCollection(targetType);

        if (!valid) {
            throw new IllegalArgumentException(String.format(
                    ERR_TYPE_NOT_ARRAY_OR_COLLECTION,
                    StringUtils.ucfirst(target),
                    resourceAnnotationType.getSimpleName(),
                    targetType.getTypeName()));
        }

        return targetType;
    }

    public static void assertNoOtherAnnotations(AnnotatedElement annotatedElement, Annotation resourceAnnotation) {
        if (annotatedElement.getDeclaredAnnotations().length > 1) {
            String target = null;
            String targetName = null;

            if (annotatedElement instanceof Field) {
                target = "Field";
                targetName = ((Field) annotatedElement).getName();
            } else if (annotatedElement instanceof Method) {
                target = "Method";
                targetName = ((Method) annotatedElement).getName();
            } else if (annotatedElement instanceof Parameter) {
                target = "Parameter";
                targetName = ((Parameter) annotatedElement).getName();
            }

            String invalidAnnotations = annotationsToString(invalidAnnotations(annotatedElement, resourceAnnotation));

            throw new BeanCreationException(String.format(
                    ERR_INVALID_ANNOTATIONS,
                    target,
                    targetName,
                    resourceAnnotation.annotationType().getSimpleName(),
                    invalidAnnotations));
        }
    }
}
