package io.hosuaby.inject.resources.junit.vintage.yaml;

import io.hosuaby.inject.resources.commons.ClassSupport;
import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;
import io.hosuaby.inject.resources.junit.vintage.helpers.ReifiedGenerics;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

/**
 * Rule representing resource with YAML content.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public YamlResource&#60;Person&#62; spongeBob = givenResource()
 *         .yaml("/io/hosuaby/sponge-bob.yaml")
 *         .parseWith(yaml);
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class YamlResource<T> extends AbstractYamlResource<T> {
    public YamlResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public YamlResource(CodeAnchor codeAnchor, String path, Charset charset, Yaml yaml) {
        super(codeAnchor, path, charset, yaml);
    }

    /** {@inheritDoc} */
    @Override
    public <U> YamlResource<U> withCharset(Charset charset) {
        return new YamlResource<>(this.codeAnchor, this.path, charset, this.yaml);
    }

    /** {@inheritDoc} */
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
