package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.commons.AnnotationSupport;
import com.adelean.inject.resources.junit.jupiter.TestWithResourcesExtension;

import java.lang.annotation.Annotation;
import java.util.Collection;

public final class Annotations {
    public static final Collection<Class<? extends Annotation>> RESOURCE_ANNOTATIONS =
            AnnotationSupport.allResourceAnnotations(TestWithResourcesExtension.class);
    public static final Collection<Class<? extends Annotation>> PARSER_ANNOTATIONS =
            AnnotationSupport.allParserAnnotations(TestWithResourcesExtension.class);

    private Annotations() {
    }

    public static Collection<Class<? extends Annotation>> allResourceAnnotations() {
        return RESOURCE_ANNOTATIONS;
    }

    public static Collection<Class<? extends Annotation>> allParserAnnotations() {
        return PARSER_ANNOTATIONS;
    }
}
