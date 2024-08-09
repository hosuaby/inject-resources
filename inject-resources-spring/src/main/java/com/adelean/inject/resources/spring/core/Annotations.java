package com.adelean.inject.resources.spring.core;

import com.adelean.inject.resources.spring.BinaryResource;
import com.adelean.inject.resources.spring.JsonLinesResource;
import com.adelean.inject.resources.spring.JsonResource;
import com.adelean.inject.resources.spring.PropertiesResource;
import com.adelean.inject.resources.spring.TextResource;
import com.adelean.inject.resources.spring.YamlDocumentsResource;
import com.adelean.inject.resources.spring.YamlResource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Annotations {
    private static final String ERR_INVALID_ANNOTATIONS =
            "%s '%s' annotated with @%s has other invalid annotations:\n%s";

    public static final Collection<Class<? extends Annotation>> RESOURCE_ANNOTATIONS = Arrays.asList(
            BinaryResource.class,
            TextResource.class,
            PropertiesResource.class,
            JsonResource.class,
            JsonLinesResource.class,
            YamlResource.class,
            YamlDocumentsResource.class);

    private Annotations() {
    }

    public static boolean isResourceAnnotation(String annotationTypeName) {
        return RESOURCE_ANNOTATIONS
                .stream()
                .map(Class::getName)
                .anyMatch(annotationTypeName::equals);
    }

    @Nullable
    public static Annotation findSingleResourceAnnotation(AnnotatedElement annotatedElement) {
        Annotation foundAnnotation = null;

        for (Class<? extends Annotation> resourceAnnotationType : RESOURCE_ANNOTATIONS) {
            Annotation annotation;
            if ((annotation = AnnotationUtils.getAnnotation(annotatedElement, resourceAnnotationType)) != null) {
                if (foundAnnotation == null) {
                    foundAnnotation = annotation;
                } else {
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

                    throw new BeanCreationException(String.format(
                            "%s '%s' has multiple resource annotations.", target, targetName));
                }
            }
        }

        return foundAnnotation;
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

    public static Annotation[] invalidAnnotations(AnnotatedElement annotatedElement, Annotation resourceAnnotation) {
        return Stream
                .of(annotatedElement.getDeclaredAnnotations())
                .filter(annotation -> !annotation.equals(resourceAnnotation))
                .toArray(Annotation[]::new);
    }

    public static String annotationsToString(Annotation[] annotations) {
        return Stream
                .of(annotations)
                .map(Annotation::annotationType)
                .map(Class::getSimpleName)
                .map(annotation -> String.format("\t- @%s", annotation))
                .collect(Collectors.joining("\n"));
    }
}
