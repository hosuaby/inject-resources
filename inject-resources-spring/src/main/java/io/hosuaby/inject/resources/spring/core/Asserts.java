package io.hosuaby.inject.resources.spring.core;

import io.hosuaby.inject.resources.core.helpers.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static io.hosuaby.inject.resources.commons.ClassSupport.isArray;
import static io.hosuaby.inject.resources.commons.ClassSupport.isCollection;

public final class Asserts {
    private static final String ERR_TYPE_NOT_ARRAY_OR_COLLECTION =
            "%s annotated with @%s must be array or collection, but was %s.";

    private Asserts() {
    }

    public static void assertArrayOrCollection(
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
    }
}
