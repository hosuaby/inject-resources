package com.adelean.inject.resources.junit.jupiter.json;

import org.jetbrains.annotations.Nullable;
import com.adelean.inject.resources.junit.jupiter.WithJacksonMapper;
import com.adelean.inject.resources.junit.jupiter.core.AbstractParserProvider;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonMapperProvider
        extends AbstractParserProvider<WithJacksonMapper, ObjectMapper, JacksonResourceParser> {
    public JacksonMapperProvider(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, WithJacksonMapper.class);
    }

    @Override
    protected JacksonResourceParser createParser(WithJacksonMapper parserAnnotation, ObjectMapper objectMapper) {
        return new JacksonResourceParser(objectMapper);
    }
}
