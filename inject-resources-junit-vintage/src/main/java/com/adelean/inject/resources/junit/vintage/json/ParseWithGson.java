package com.adelean.inject.resources.junit.vintage.json;

import com.adelean.inject.resources.core.function.ThrowingFunction;
import com.google.gson.Gson;

import java.io.Reader;
import java.lang.reflect.Type;

public final class ParseWithGson<T> implements ThrowingFunction<Reader, T> {
    private final Gson gson;
    private final Type targetType;

    ParseWithGson(Object gson, Type targetType) {
        this.gson = (Gson) gson;
        this.targetType = targetType;
    }

    @Override
    public T apply(Reader reader) {
        return gson.fromJson(reader, targetType);
    }
}
