package com.adelean.junit.jupiter.resources.json;

import java.lang.reflect.Type;

import com.adelean.junit.jupiter.resources.dsl.ResourceAsReader;
import com.google.gson.Gson;

public final class GsonResourceParser implements JsonParser {
    private final Gson gson;

    public GsonResourceParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object parse(ResourceAsReader resource, Type valueType) {
        return resource.parse(resourceReader -> gson.fromJson(resourceReader, valueType));
    }

    @Override
    public Object parse(String source, Type valueType) {
        return gson.fromJson(source, valueType);
    }
}
