package io.hosuaby.inject.resources.commons;

import static io.hosuaby.inject.resources.commons.Errors.typesToString;
import static io.hosuaby.inject.resources.core.helpers.StringUtils.ucfirst;
import static java.lang.reflect.Modifier.isPrivate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import io.hosuaby.inject.resources.annotations.SupportedTypes;

public final class FieldAsserts {
    private static final String ERR_PRIVATE_FIELD = "@%s field [%s] must not be private.";
    private static final String ERR_UNSUPPORTED_TYPE =
            "@%s cannot be resolved on %s of type %s. Supported types are:\n%s";
    private static final String ERR_TARGET_NOT_ARRAY_OR_COLLECTION =
            "@%s cannot be resolved on %s of type %s. %s must be array or collection.";

    private FieldAsserts() {
    }

    public static void assertNonPrivate(Field field, Class<?> annotationType) {
        if (isPrivate(field.getModifiers())) {
            throw new RuntimeException(String.format(
                    ERR_PRIVATE_FIELD, annotationType.getSimpleName(), field.getName()));
        }
    }

    public static void assertSupportedType(Field field, Class<? extends Annotation> annotationType) {
        Class<?> fieldType = field.getType();
        assertSupportedType("field", fieldType, annotationType);
    }

    public static void assertSupportedType(Parameter parameter, Class<? extends Annotation> annotationType) {
        Class<?> parameterType = parameter.getType();
        assertSupportedType("parameter", parameterType, annotationType);
    }

    private static void assertSupportedType(
            String target,
            Class<?> targetType,
            Class<? extends Annotation> annotationType) {
        SupportedTypes supportedTypes = annotationType.getAnnotation(SupportedTypes.class);

        if (supportedTypes == null) {
            return;
        }

        for (Class<?> supportedType : supportedTypes.value()) {
            if (targetType.isAssignableFrom(supportedType)) {
                return;
            }
        }

        throw new RuntimeException(String.format(ERR_UNSUPPORTED_TYPE,
                annotationType.getSimpleName(),
                target,
                targetType.getName(),
                typesToString(supportedTypes.value())));
    }

    public static void assertArrayOrCollection(
            String target,
            Class<?> targetType,
            Class<? extends Annotation> annotationType) {
        boolean valid = ClassSupport.isArray(targetType) || ClassSupport.isCollection(targetType);

        if (!valid) {
            throw new RuntimeException(String.format(
                    ERR_TARGET_NOT_ARRAY_OR_COLLECTION,
                    annotationType.getSimpleName(),
                    target,
                    targetType.getName(),
                    ucfirst(target)));
        }
    }
}
