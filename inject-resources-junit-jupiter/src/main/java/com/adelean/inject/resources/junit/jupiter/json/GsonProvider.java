package com.adelean.inject.resources.junit.jupiter.json;

import org.jetbrains.annotations.Nullable;
import com.adelean.inject.resources.junit.jupiter.WithGson;
import com.adelean.inject.resources.junit.jupiter.core.AbstractParserProvider;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.google.gson.Gson;

public final class GsonProvider
        extends AbstractParserProvider<WithGson, Gson, GsonResourceParser> {
    public GsonProvider(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, WithGson.class);
    }

    @Override
    protected GsonResourceParser createParser(WithGson parserAnnotation, Gson gson) {
        return new GsonResourceParser(gson);
    }
}
