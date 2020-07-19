package com.adelean.inject.resources.junit.vintage.json;

import com.adelean.inject.resources.core.function.ThrowingFunction;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.Reader;
import java.lang.reflect.Type;

public final class ParseWithJackson<T> implements ThrowingFunction<Reader, T> {
    private final ObjectReader objectReader;

    ParseWithJackson(Object objectMapper, Type targetType) {
        ObjectMapper jacksonMapper = (ObjectMapper) objectMapper;
        TypeFactory typeFactory = jacksonMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(targetType);
        this.objectReader = jacksonMapper.readerFor(javaType);
    }

    @Override
    public T apply(Reader reader) throws Throwable {
        return this.objectReader.readValue(reader);
    }
}
