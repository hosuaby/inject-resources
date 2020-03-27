package com.adelean.junit.jupiter.resources.core.helpers;

import static org.junit.platform.commons.support.ModifierSupport.isPrivate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import com.adelean.junit.jupiter.resources.core.annotations.SupportedTypes;

public final class FieldAsserts {
    private static final String ERR_PRIVATE_FIELD = "@%s field [%s] must not be private.";
    private static final String ERR_UNSUPPORTED_TYPE =
            "@%s cannot be resolved on %s of type %s. Supported types are:\n%s";
    private static final String ERR_CONSTRUCTOR_INJECTION_UNSUPPORTED =
            "@%s is not supported on constructor parameters. Please use field injection instead.";
    private static final String ERR_STATIC_METHOD_INJECTION_UNSUPPORTED =
            "@%s is not supported on parameters of static methods. Please use instance method parameter injection "
                    + "instead.";

    private FieldAsserts() {
    }

    public static void assertNonPrivate(Field field, Class<?> annotationType) {
        if (isPrivate(field)) {
            throw new ExtensionConfigurationException(String.format(
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

        throw new ExtensionConfigurationException(String.format(ERR_UNSUPPORTED_TYPE,
                annotationType.getSimpleName(),
                target,
                targetType.getName(),
                typesToString(supportedTypes.value())));
    }

    public static void assertNotConstructor(Executable executable, Class<? extends Annotation> annotationType) {
        if (executable instanceof Constructor) {
            throw new ParameterResolutionException(
                    String.format(ERR_CONSTRUCTOR_INJECTION_UNSUPPORTED, annotationType.getSimpleName()));
        }
    }

    public static void assertNotStaticMethod(Executable executable, Class<? extends Annotation> annotationType) {
        if (executable.getAnnotatedReceiverType() == null) {    // true if method is static or constructor
            throw new ParameterResolutionException(
                    String.format(ERR_STATIC_METHOD_INJECTION_UNSUPPORTED, annotationType.getSimpleName()));
        }
    }

    static String typesToString(Class<?>... types) {
        return Stream
                .of(types)
                .map(type -> String.format("\t- %s\n", type.getName()))
                .collect(Collectors.joining());
    }
}
