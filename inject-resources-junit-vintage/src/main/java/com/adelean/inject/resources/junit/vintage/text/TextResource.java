package com.adelean.inject.resources.junit.vintage.text;

import static com.adelean.inject.resources.core.InjectResources.resource;

import com.adelean.inject.resources.junit.vintage.core.AbstractTextResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.nio.charset.Charset;

/**
 * Rule representing resource with textual content.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public TextResource textResource = givenResource()
 *         .text("/com/adelean/junit/jupiter/resource.txt")
 *         .withCharset(StandardCharsets.UTF_8);
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class TextResource extends AbstractTextResource<String> {
    public TextResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public TextResource(CodeAnchor codeAnchor, String path, Charset charset) {
        super(codeAnchor, path, charset);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public TextResource withCharset(Charset charset) {
        return new TextResource(this.codeAnchor, this.path, charset);
    }

    @Override
    protected String load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .text(charset);
    }
}
