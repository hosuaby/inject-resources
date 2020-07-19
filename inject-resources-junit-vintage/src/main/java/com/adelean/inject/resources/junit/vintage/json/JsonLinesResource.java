package com.adelean.inject.resources.junit.vintage.json;

import com.adelean.inject.resources.commons.CollectionFactory;
import com.adelean.inject.resources.core.ResourceAsLines;
import com.adelean.inject.resources.core.function.ThrowingFunction;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import com.adelean.inject.resources.junit.vintage.helpers.ReifiedGenerics;
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

import static com.adelean.inject.resources.commons.ClassSupport.elementType;
import static com.adelean.inject.resources.commons.ClassSupport.isArray;
import static com.adelean.inject.resources.commons.ClassSupport.isCollection;
import static com.adelean.inject.resources.core.InjectResources.resource;
import static com.adelean.inject.resources.junit.vintage.helpers.Asserts.assertArrayOrCollection;

/**
 * @author Alexei KLENIN
 */
public final class JsonLinesResource<T> extends AbstractJsonResource<T> {
    public JsonLinesResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public JsonLinesResource(CodeAnchor codeAnchor, String path, Charset charset, Object parser) {
        super(codeAnchor, path, charset, parser);
    }

    @Override
    public <U> JsonLinesResource<U> withCharset(Charset charset) {
        return new JsonLinesResource<>(this.codeAnchor, this.path, charset, this.parser);
    }

    @Override
    public <U> JsonLinesResource<U> parseWith(Object parser) {
        return new JsonLinesResource<>(this.codeAnchor, this.path, this.charset, parser);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected T load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        Type targetType = assertArrayOrCollection(ReifiedGenerics.targetType(testClass, this), getClass());
        Type elementType = elementType(targetType);

        ThrowingFunction<Reader, T> parseFunction = parseFunction(assertHasParser(), elementType);

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
