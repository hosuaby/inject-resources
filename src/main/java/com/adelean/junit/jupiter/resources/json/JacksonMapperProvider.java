package com.adelean.junit.jupiter.resources.json;

import com.adelean.junit.jupiter.resources.WithJacksonMapper;
import com.adelean.junit.jupiter.resources.common.InjectionContext;
import com.adelean.junit.jupiter.resources.common.ParserProvider;
import org.junit.platform.commons.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class JacksonMapperProvider extends ParserProvider<WithJacksonMapper> {
    public JacksonMapperProvider(
            InjectionContext injectionContext,
            Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, WithJacksonMapper.class);
    }

    @Override
    protected String parserNameFromMethod(Method method, WithJacksonMapper annotation) {
        String parserName = StringUtils.isNotBlank(annotation.name())
                ? annotation.name()
                : annotation.value();

        return StringUtils.isNotBlank(parserName)
                ? parserName
                : super.parserNameFromMethod(method, annotation);
    }

    @Override
    protected String parserNameFromField(Field instanceField, WithJacksonMapper annotation) {
        String parserName = StringUtils.isNotBlank(annotation.name())
                ? annotation.name()
                : annotation.value();

        return StringUtils.isNotBlank(parserName)
                ? parserName
                : super.parserNameFromField(instanceField, annotation);
    }
}
