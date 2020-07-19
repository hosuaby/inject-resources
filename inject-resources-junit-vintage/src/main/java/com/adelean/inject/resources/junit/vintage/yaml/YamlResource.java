package com.adelean.inject.resources.junit.vintage.yaml;

import com.adelean.inject.resources.commons.ClassSupport;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import com.adelean.inject.resources.junit.vintage.helpers.ReifiedGenerics;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static com.adelean.inject.resources.core.InjectResources.resource;

/**
 * @author Alexei KLENIN
 */
public final class YamlResource<T> extends AbstractYamlResource<T> {
    public YamlResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public YamlResource(CodeAnchor codeAnchor, String path, Charset charset, Yaml yaml) {
        super(codeAnchor, path, charset, yaml);
    }

    @Override
    public <U> YamlResource<U> withCharset(Charset charset) {
        return new YamlResource<>(this.codeAnchor, this.path, charset, this.yaml);
    }

    @Override
    public <U> YamlResource<U> parseWith(Yaml yaml) {
        return new YamlResource<>(this.codeAnchor, this.path, this.charset, yaml);
    }

    @Override
    protected T load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        Type targetType = ReifiedGenerics.targetType(testClass, this);

        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) ClassSupport.fromType(targetType);

        Yaml parser = assertHasYaml();

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset)
                .parseChecked(reader -> parser.loadAs(reader, targetClass));
    }
}
