package com.adelean.inject.resources.junit.jupiter.core;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Reflections {

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getDefaultConstructor(Class<T> type) {
        return ReflectionUtils
                .getConstructors(type)
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException(String.format(
                        "%s has no constructor", type.getSimpleName())));
    }

    public static Field makeAccessibleField(Field field) {
        if ((!Modifier.isPublic(field.getModifiers())
                || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers()))
                && !field.isAccessible()) {
            field.setAccessible(true);
        }

        return field;
    }

    public static Method makeAccessibleMethod(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        return method;
    }
}
