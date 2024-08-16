package io.hosuaby.inject.resources.junit.jupiter.json;

import io.hosuaby.inject.resources.core.ResourceAsReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Type;

public final class JacksonResourceParser implements JsonParser {
    private final ObjectMapper objectMapper;

    public JacksonResourceParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object parse(ResourceAsReader resource, Type valueType) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(valueType);

        return resource.parseChecked(resourceReader -> objectMapper.readValue(resourceReader, javaType));
    }

    @Override
    public Object parse(String source, Type valueType) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(valueType);

        try {
            return objectMapper.readValue(source, javaType);
        } catch (JsonProcessingException jsonParsingException) {
            throw new RuntimeException(jsonParsingException);
        }
    }
}
