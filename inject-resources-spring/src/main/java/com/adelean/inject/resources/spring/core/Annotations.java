package com.adelean.inject.resources.spring.core;

import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.spring.EnableResourceInjection;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Annotations {
    public static final Collection<Class<? extends Annotation>> RESOURCE_ANNOTATIONS =
            AnnotationSupport.allResourceAnnotations(EnableResourceInjection.class);

    private Annotations() {
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
