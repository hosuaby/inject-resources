package com.adelean.inject.resources.junit.vintage.properties;

import static com.adelean.inject.resources.core.InjectResources.resource;

import com.adelean.inject.resources.junit.vintage.core.AbstractTextResource;
import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Rule representing resource with java properties content.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public PropertiesResource dbProperties = givenResource()
 *         .properties("/com/adelean/junit/jupiter/db.properties");
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class PropertiesResource extends AbstractTextResource<Properties> {
    public PropertiesResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    public PropertiesResource(CodeAnchor codeAnchor, String path, Charset charset) {
        super(codeAnchor, path, charset);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public PropertiesResource withCharset(Charset charset) {
        return new PropertiesResource(this.codeAnchor, this.path, charset);
    }

    @Override
    protected Properties load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();

        Properties properties = new Properties();
        resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .asReader(charset)
                .thenChecked(properties::load);

        return properties;
    }
}
