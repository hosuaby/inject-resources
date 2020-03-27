package com.adelean.junit.jupiter.resources.json;

import java.lang.reflect.Type;

import com.adelean.junit.jupiter.resources.core.ResourceParser;
import com.adelean.junit.jupiter.resources.dsl.ResourceAsReader;

interface JsonParser extends ResourceParser<ResourceAsReader, Object> {
    Object parse(String source, Type valueType);
}
