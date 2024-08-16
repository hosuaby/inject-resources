package io.hosuaby.inject.resources.junit.vintage.yaml;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import io.hosuaby.inject.resources.commons.ClassSupport;
import io.hosuaby.inject.resources.commons.CollectionFactory;
import io.hosuaby.inject.resources.core.ResourceAsReader;
import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;
import io.hosuaby.inject.resources.junit.vintage.helpers.ReifiedGenerics;
import io.hosuaby.inject.resources.junit.vintage.helpers.Asserts;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.stream.StreamSupport;

/**
 * Rule representing resource with YAML containing multiple documents separated by three hyphens "---".
 *
 * <p>Can convert YAML documents into array or collection.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public YamlDocumentsResource&#60;ArrayList&#60;Log&#62;&#62; logsAsArrayList = givenResource()
 *         .yamlDocuments("/io/hosuaby/logs.yml")
 *         .parseWith(logParser);
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class YamlDocumentsResource<T> extends AbstractYamlResource<T> {
    public YamlDocumentsResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public YamlDocumentsResource(CodeAnchor codeAnchor, String path, Charset charset, Yaml yaml) {
        super(codeAnchor, path, charset, yaml);
    }

    /** {@inheritDoc} */
    @Override
    public <U> YamlDocumentsResource<U> withCharset(Charset charset) {
        return new YamlDocumentsResource<>(this.codeAnchor, this.path, charset, this.yaml);
    }

    /** {@inheritDoc} */
    @Override
    public <U> YamlDocumentsResource<U> parseWith(Yaml yaml) {
        return new YamlDocumentsResource<>(this.codeAnchor, this.path, this.charset, yaml);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected T load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        Type targetType = Asserts.assertArrayOrCollection(ReifiedGenerics.targetType(testClass, this), getClass());

        Class<?> elementType = ClassSupport.fromType(ClassSupport.elementType(targetType));

        Yaml parser = assertHasYaml();

        try (ResourceAsReader resource = resource()
                     .onClassLoaderOf(testClass)
                     .withPath(path)
                     .asReader(charset)) {
            Iterable<Object> documents = parser.loadAll(resource.reader());

            if (ClassSupport.isArray(targetType)) {
                return (T) StreamSupport
                        .stream(documents.spliterator(), false)
                        .toArray(length -> (Object[]) Array.newInstance(elementType, length));
            } else if (ClassSupport.isCollection(targetType)) {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) targetType);
                documents.forEach(collection::add);
                return (T) collection;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }
}
