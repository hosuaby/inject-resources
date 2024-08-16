package io.hosuaby.inject.resources.junit.vintage.json;

import io.hosuaby.inject.resources.core.function.ThrowingFunction;
import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;
import io.hosuaby.inject.resources.junit.vintage.helpers.ReifiedGenerics;
import io.hosuaby.inject.resources.parsers.Parsers;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

/**
 * Rule representing resource with JSON content.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public JsonResource&#60;Person&#62; spongeBob = givenResource()
 *         .json("/io/hosuaby/sponge-bob.json")
 *         .withCharset(StandardCharsets.UTF_8)
 *         .parseWith(objectMapper);
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class JsonResource<T> extends AbstractJsonResource<T> {
    public JsonResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public JsonResource(CodeAnchor codeAnchor, String path, Charset charset, Object parser) {
        super(codeAnchor, path, charset, parser);
    }

    /** {@inheritDoc} */
    @Override
    public <U> JsonResource<U> withCharset(Charset charset) {
        return new JsonResource<>(this.codeAnchor, this.path, charset, this.parser);
    }

    /** {@inheritDoc} */
    @Override
    public <U> JsonResource<U> parseWith(Object parser) {
        return new JsonResource<>(this.codeAnchor, this.path, this.charset, parser);
    }

    @Override
    protected T load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        Type targetType = ReifiedGenerics.targetType(testClass, this);
        ThrowingFunction<Reader, T> parseFunction = Parsers.parseFunction(assertHasParser(), targetType);

        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset)
                .parseChecked(parseFunction);
    }
}
