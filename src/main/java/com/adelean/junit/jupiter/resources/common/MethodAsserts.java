package com.adelean.junit.jupiter.resources.common;

import static org.junit.platform.commons.support.ModifierSupport.isPrivate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;

public final class MethodAsserts {
    private static final String ERR_PRIVATE_METHOD = "@%s method [%s] must not be private.";
    private static final String ERR_METHOD_HAS_ARGUMENTS = "@%s method [%s] must have no arguments.";
    private static final String ERR_METHOD_RETURNS_UNSUPPORTED_TYPE =
            "@%s method [%s] returns unsupported type %s. Must be %s.";

    private MethodAsserts() {
    }

    public static void assertNonPrivate(Method method, Class<?> annotationType) {
        if (isPrivate(method)) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_PRIVATE_METHOD, annotationType.getSimpleName(), method.getName()));
        }
    }

    public static void assertNoArguments(Method method, Class<?> annotationType) {
        if (method.getParameterCount() > 0) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_METHOD_HAS_ARGUMENTS, annotationType.getSimpleName(), method.getName()));
        }
    }

    public static void assertReturnsSupportedType(Method method, Class<? extends Annotation> annotationType) {
        SupportedTypes supportedTypes = annotationType.getAnnotation(SupportedTypes.class);

        if (supportedTypes == null) {
            return;
        }

        Class<?> supportedType = supportedTypes.value()[0];

        if (method.getReturnType().isAssignableFrom(supportedType)) {
            return;
        }

        throw new ExtensionConfigurationException(String.format(ERR_METHOD_RETURNS_UNSUPPORTED_TYPE,
                annotationType.getSimpleName(),
                method.getName(),
                method.getReturnType().getName(),
                supportedType.getName()));
    }
}
