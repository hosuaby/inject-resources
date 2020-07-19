package com.adelean.inject.resources.junit.vintage.text;

import static com.adelean.inject.resources.core.InjectResources.resource;

import com.adelean.inject.resources.junit.vintage.core.AbstractTextResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.nio.charset.Charset;

/**
 * @author Alexei KLENIN
 */
public final class TextResource extends AbstractTextResource<String> {
    public TextResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public TextResource(CodeAnchor codeAnchor, String path, Charset charset) {
        super(codeAnchor, path, charset);
    }

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
