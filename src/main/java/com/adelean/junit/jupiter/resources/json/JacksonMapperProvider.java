package com.adelean.junit.jupiter.resources.json;

import org.jetbrains.annotations.Nullable;
import com.adelean.junit.jupiter.resources.WithJacksonMapper;
import com.adelean.junit.jupiter.resources.core.AbstractParserProvider;
import com.adelean.junit.jupiter.resources.core.cdi.InjectionContext;
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
