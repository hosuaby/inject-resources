package io.hosuaby.inject.resources.commons;

import static java.lang.reflect.Modifier.isPrivate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

import io.hosuaby.inject.resources.annotations.SupportedTypes;

public final class MethodAsserts {
    private static final String ERR_PRIVATE_METHOD = "@%s method [%s] must not be private.";
    private static final String ERR_METHOD_HAS_ARGUMENTS = "@%s method [%s] must have no arguments.";
    private static final String ERR_CONSTRUCTOR_INJECTION_UNSUPPORTED =
            "@%s is not supported on constructor parameters. Please use field injection instead.";
    private static final String ERR_STATIC_METHOD_INJECTION_UNSUPPORTED =
            "@%s is not supported on parameters of static methods. Please use instance method parameter injection "
                    + "instead.";
    private static final String ERR_METHOD_RETURNS_UNSUPPORTED_TYPE =
            "@%s method [%s] returns unsupported type %s. Must be %s.";

    private MethodAsserts() {
    }

    public static void assertNonPrivate(Method method, Class<?> annotationType) {
        if (isPrivate(method.getModifiers())) {
            throw new RuntimeException(String.format(
                    ERR_PRIVATE_METHOD, annotationType.getSimpleName(), method.getName()));
        }
    }

    public static void assertNoArguments(Method method, Class<?> annotationType) {
        if (method.getParameterCount() > 0) {
            throw new RuntimeException(String.format(
                    ERR_METHOD_HAS_ARGUMENTS, annotationType.getSimpleName(), method.getName()));
        }
    }

    public static void assertNotConstructor(Executable executable, Class<? extends Annotation> annotationType) {
        if (executable instanceof Constructor) {
            throw new RuntimeException(
                    String.format(ERR_CONSTRUCTOR_INJECTION_UNSUPPORTED, annotationType.getSimpleName()));
        }
    }

    public static void assertNotStaticMethod(Executable executable, Class<? extends Annotation> annotationType) {
        if (executable.getAnnotatedReceiverType() == null) {    // true if method is static or constructor
            throw new RuntimeException(
                    String.format(ERR_STATIC_METHOD_INJECTION_UNSUPPORTED, annotationType.getSimpleName()));
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

        throw new RuntimeException(String.format(ERR_METHOD_RETURNS_UNSUPPORTED_TYPE,
                annotationType.getSimpleName(),
                method.getName(),
                method.getReturnType().getName(),
                supportedType.getName()));
    }
}
