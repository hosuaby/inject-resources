package io.hosuaby.inject.resources.junit.jupiter.yaml;

import java.lang.annotation.Annotation;

import io.hosuaby.inject.resources.junit.jupiter.core.AbstractResourcesInjector;
import io.hosuaby.inject.resources.junit.jupiter.core.cdi.InjectionContext;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;
import io.hosuaby.inject.resources.commons.ClasspathSupport;

abstract class AbstractYamlResourcesInjector<A extends Annotation> extends AbstractResourcesInjector<A> {
    private static final String ERR_SNAKE_YAML_MISSING_ON_CLASS_PATH = "Snakeyaml is missing on classpath.";

    protected AbstractYamlResourcesInjector(
            InjectionContext injectionContext,
            @Nullable Object testInstance,
            Class<?> testClass,
            Class<A> annotationType) {
        super(injectionContext, testInstance, testClass, annotationType);
    }

    protected SnakeYamlResourceParser findSnakeYamlParser(A resourceAnnotation) {
        String yamlParserName = yamlParserName(resourceAnnotation);
        return injectionContext
                .findBean(testClass, yamlParserName, SnakeYamlResourceParser.class)
                .orElseGet(this::defaultYamlParser);
    }

    @Nullable
    protected abstract String yamlParserName(A resourceAnnotation);

    SnakeYamlResourceParser defaultYamlParser() {
        if (ClasspathSupport.isSnakeYamlPresent()) {
            Yaml yaml = new Yaml();
            return new SnakeYamlResourceParser(yaml);
        } else {
            throw new RuntimeException(ERR_SNAKE_YAML_MISSING_ON_CLASS_PATH);
        }
    }
}
