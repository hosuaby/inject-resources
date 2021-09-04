package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.junit.jupiter.GivenBinaryResource;
import com.adelean.inject.resources.junit.jupiter.GivenJsonLinesResource;
import com.adelean.inject.resources.junit.jupiter.GivenJsonResource;
import com.adelean.inject.resources.junit.jupiter.GivenPropertiesResource;
import com.adelean.inject.resources.junit.jupiter.GivenTextResource;
import com.adelean.inject.resources.junit.jupiter.GivenYamlDocumentsResource;
import com.adelean.inject.resources.junit.jupiter.GivenYamlResource;
import com.adelean.inject.resources.junit.jupiter.WithGson;
import com.adelean.inject.resources.junit.jupiter.WithJacksonMapper;
import com.adelean.inject.resources.junit.jupiter.WithSnakeYaml;

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
