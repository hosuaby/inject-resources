package com.adelean.inject.resources.junit.jupiter.json;

import java.lang.reflect.Type;

import com.adelean.inject.resources.core.ResourceAsReader;
import com.adelean.inject.resources.junit.jupiter.core.ResourceParser;

interface JsonParser extends ResourceParser<ResourceAsReader, Object> {
    Object parse(String source, Type valueType);
}
