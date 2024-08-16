package io.hosuaby.inject.resources.junit.jupiter.json;

import java.lang.reflect.Type;

import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.jupiter.core.ResourceParser;

interface JsonParser extends ResourceParser<ResourceAsReader, Object> {
    Object parse(String source, Type valueType);
}
