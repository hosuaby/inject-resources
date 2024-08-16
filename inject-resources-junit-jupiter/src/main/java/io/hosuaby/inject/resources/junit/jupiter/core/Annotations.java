package io.hosuaby.inject.resources.junit.jupiter.core;

import io.hosuaby.inject.resources.junit.jupiter.GivenBinaryResource;
import io.hosuaby.inject.resources.junit.jupiter.GivenJsonLinesResource;
import io.hosuaby.inject.resources.junit.jupiter.GivenJsonResource;
import io.hosuaby.inject.resources.junit.jupiter.GivenPropertiesResource;
import io.hosuaby.inject.resources.junit.jupiter.GivenTextResource;
import io.hosuaby.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import io.hosuaby.inject.resources.junit.jupiter.GivenYamlResource;
import io.hosuaby.inject.resources.junit.jupiter.WithGson;
import io.hosuaby.inject.resources.junit.jupiter.WithJacksonMapper;
import io.hosuaby.inject.resources.junit.jupiter.WithSnakeYaml;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

public final class Annotations {
    public static final Collection<Class<? extends Annotation>> RESOURCE_ANNOTATIONS = Arrays.asList(
            GivenBinaryResource.class,
            GivenTextResource.class,
            GivenPropertiesResource.class,
            GivenJsonResource.class,
            GivenJsonLinesResource.class,
            GivenYamlResource.class,
            GivenYamlDocumentsResource.class);
    public static final Collection<Class<? extends Annotation>> PARSER_ANNOTATIONS = Arrays.asList(
            WithJacksonMapper.class,
            WithGson.class,
            WithSnakeYaml.class);

    private Annotations() {
    }
}
