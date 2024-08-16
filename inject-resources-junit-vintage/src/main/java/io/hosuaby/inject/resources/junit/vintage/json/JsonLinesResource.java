package io.hosuaby.inject.resources.junit.vintage.json;

import io.hosuaby.inject.resources.commons.CollectionFactory;
import io.hosuaby.inject.resources.core.ResourceAsLines;
import io.hosuaby.inject.resources.core.function.ThrowingFunction;
import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;
import io.hosuaby.inject.resources.junit.vintage.helpers.ReifiedGenerics;
import io.hosuaby.inject.resources.junit.vintage.helpers.Asserts;
import io.hosuaby.inject.resources.parsers.Parsers;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.stream.Stream;

import static io.hosuaby.inject.resources.commons.ClassSupport.elementType;
import static io.hosuaby.inject.resources.commons.ClassSupport.isArray;
import static io.hosuaby.inject.resources.commons.ClassSupport.isCollection;
import static io.hosuaby.inject.resources.core.InjectResources.resource;

/**
 * Rule representing resource with content in format JSON Lines (one JSON document per line).
 *
 * <p>Can convert JSON Lines into array or collection.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public JsonLinesResource&#60;Log[]&#62; logsAsArray = givenResource()
 *         .jsonLines("/io/hosuaby/logs.jsonl")
 *         .parseWith(objectMapper);
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class JsonLinesResource<T> extends AbstractJsonResource<T> {
    public JsonLinesResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public JsonLinesResource(CodeAnchor codeAnchor, String path, Charset charset, Object parser) {
        super(codeAnchor, path, charset, parser);
    }

    /** {@inheritDoc} */
    @Override
    public <U> JsonLinesResource<U> withCharset(Charset charset) {
        return new JsonLinesResource<>(this.codeAnchor, this.path, charset, this.parser);
    }

    /** {@inheritDoc} */
    @Override
    public <U> JsonLinesResource<U> parseWith(Object parser) {
        return new JsonLinesResource<>(this.codeAnchor, this.path, this.charset, parser);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected T load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        Type targetType = Asserts.assertArrayOrCollection(ReifiedGenerics.targetType(testClass, this), getClass());
        Type elementType = elementType(targetType);

        ThrowingFunction<Reader, T> parseFunction = Parsers.parseFunction(assertHasParser(), elementType);

        try (ResourceAsLines resource = resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asLines(charset)) {
            Stream<Object> items = resource.parseLinesChecked(line -> {
                StringReader lineReader = new StringReader(line);
                return parseFunction.apply(lineReader);
            });

            if (isArray(targetType)) {
                return (T) items.toArray(length -> (Object[]) Array.newInstance((Class<?>) elementType, length));
            } else if (isCollection(targetType))  {
                Collection collection = CollectionFactory.newCollection((ParameterizedType) targetType);
                items.forEach(collection::add);
                return (T) collection;
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return null;
    }
}
