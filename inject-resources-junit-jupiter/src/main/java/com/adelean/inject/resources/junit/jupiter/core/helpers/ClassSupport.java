package com.adelean.inject.resources.junit.jupiter.core.helpers;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.jetbrains.annotations.Nullable;

public final class ClassSupport {
    private ClassSupport() {
    }

    public static boolean isSubclass(Class<?> clazz, Class<?> superClass) {
        return clazz != superClass && superClass.isAssignableFrom(clazz);
    }

    public static boolean isArray(Type type) {
        return type instanceof GenericArrayType
                || type instanceof Class && ((Class<?>) type).isArray();
    }

    public static boolean isCollection(Type type) {
        return type instanceof ParameterizedType
                && Collection.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
    }

    public static Class<?> fromType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else {
            return Object.class;
        }
    }

    /**
     * Returns element type stored in {@code arrayOrCollectionType}.
     *
     * @param arrayOrCollectionType  type of array or collection
     * @return type of elements stored in array or collection
     */
    @Nullable
    public static Type elementType(Type arrayOrCollectionType) {
        Type elementType = null;

        if (ClassSupport.isArray(arrayOrCollectionType)) {
            if (arrayOrCollectionType instanceof GenericArrayType) {
                return ((GenericArrayType) arrayOrCollectionType).getGenericComponentType();
            } else if (arrayOrCollectionType instanceof Class) {
                Class<?> arrayType = (Class<?>) arrayOrCollectionType;
                elementType = arrayType.getComponentType();
            }
        } else if (ClassSupport.isCollection(arrayOrCollectionType)) {
            ParameterizedType collectionType = (ParameterizedType) arrayOrCollectionType;
            elementType = collectionType.getActualTypeArguments()[0];
        }

        return elementType;
    }
}
