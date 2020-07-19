package com.adelean.inject.resources.junit.vintage;

import com.adelean.inject.resources.junit.vintage.binary.BinaryResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import com.adelean.inject.resources.junit.vintage.json.JsonLinesResource;
import com.adelean.inject.resources.junit.vintage.json.JsonResource;
import com.adelean.inject.resources.junit.vintage.properties.PropertiesResource;
import com.adelean.inject.resources.junit.vintage.text.TextResource;
import com.adelean.inject.resources.junit.vintage.yaml.YamlDocumentsResource;
import com.adelean.inject.resources.junit.vintage.yaml.YamlResource;

/**
 * @author Alexei KLENIN
 */
public final class GivenResource {
    final CodeAnchor codeAnchor;

    private GivenResource(CodeAnchor codeAnchor) {
        this.codeAnchor = codeAnchor;
    }

    public static GivenResource givenResource() {
        StackTraceElement callerTrace = Thread.currentThread().getStackTrace()[2];
        CodeAnchor codeAnchor = new CodeAnchor(callerTrace);
        return new GivenResource(codeAnchor);
    }

    public BinaryResource binary(String firstPathToken, String... otherTokens) {
        return new BinaryResource(codeAnchor, firstPathToken, otherTokens);
    }

    public TextResource text(String firstPathToken, String... otherTokens) {
        return new TextResource(codeAnchor, firstPathToken, otherTokens);
    }

    public PropertiesResource properties(String firstPathToken, String... otherTokens) {
        return new PropertiesResource(codeAnchor, firstPathToken, otherTokens);
    }

    public <T> JsonResource<T> json(String firstPathToken, String... otherTokens) {
        return new JsonResource<>(codeAnchor, firstPathToken, otherTokens);
    }

    public <T> JsonLinesResource<T> jsonLines(String firstPathToken, String... otherTokens) {
        return new JsonLinesResource<>(codeAnchor, firstPathToken, otherTokens);
    }

    public <T> YamlResource<T> yaml(String firstPathToken, String... otherTokens) {
        return new YamlResource<>(codeAnchor, firstPathToken, otherTokens);
    }

    public <T>YamlDocumentsResource<T> yamlDocuments(String firstPathToken, String... otherTokens) {
        return new YamlDocumentsResource<>(codeAnchor, firstPathToken, otherTokens);
    }
}
