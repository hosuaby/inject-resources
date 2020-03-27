package com.adelean.junit.jupiter.resources.json;

import org.jetbrains.annotations.Nullable;
import com.adelean.junit.jupiter.resources.WithGson;
import com.adelean.junit.jupiter.resources.core.AbstractParserProvider;
import com.adelean.junit.jupiter.resources.core.cdi.InjectionContext;
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
