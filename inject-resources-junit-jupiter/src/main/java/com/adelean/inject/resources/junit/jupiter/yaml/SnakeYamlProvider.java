package com.adelean.inject.resources.junit.jupiter.yaml;

import com.adelean.inject.resources.junit.jupiter.WithSnakeYaml;
import com.adelean.inject.resources.junit.jupiter.core.AbstractParserProvider;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

public final class SnakeYamlProvider extends AbstractParserProvider<WithSnakeYaml, Yaml, SnakeYamlResourceParser> {
    public SnakeYamlProvider(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass) {
        super(injectionContext, testInstance, testClass, WithSnakeYaml.class);
    }

    @Override
    protected SnakeYamlResourceParser createParser(WithSnakeYaml parserAnnotation, Yaml parser) {
        return new SnakeYamlResourceParser(parser);
    }
}
