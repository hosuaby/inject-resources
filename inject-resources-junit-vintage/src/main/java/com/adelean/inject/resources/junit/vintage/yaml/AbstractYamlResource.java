package com.adelean.inject.resources.junit.vintage.yaml;

import com.adelean.inject.resources.junit.vintage.core.AbstractTextResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.Charset;
import java.util.Objects;

public abstract class AbstractYamlResource<T> extends AbstractTextResource<T> {
    protected final Yaml yaml;

    protected AbstractYamlResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
        this.yaml = null;
    }

    protected AbstractYamlResource(CodeAnchor codeAnchor, String path, Charset charset, Yaml yaml) {
        super(codeAnchor, path, charset);
        this.yaml = yaml;
    }

    @NotNull
    protected Yaml assertHasYaml() {
        return Objects.requireNonNull(
                yaml, String.format("Rule %s must have defined Yaml object.", getClass().getSimpleName()));
    }

    public abstract <U> AbstractYamlResource<U> parseWith(Yaml yaml);
}
