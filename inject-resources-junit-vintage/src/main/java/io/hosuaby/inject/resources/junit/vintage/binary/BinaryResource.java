package io.hosuaby.inject.resources.junit.vintage.binary;

import static io.hosuaby.inject.resources.core.InjectResources.resource;

import io.hosuaby.inject.resources.junit.vintage.core.AbstractResourceRule;
import io.hosuaby.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Rule representing resource with binary content.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;Rule
 * public BinaryResource fibonacci = givenResource()
 *         .binary("/io/hosuaby/fibonacci.bin");
 * </pre>
 *
 * @author Alexei KLENIN
 */
public final class BinaryResource extends AbstractResourceRule<byte[]> {
    public BinaryResource(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        super(codeAnchor, firstPathToken, otherTokens);
    }

    @Override
    protected byte[] load(Statement base, Description description) {
        Class<?> testClass = description.getTestClass();
        return resource()
                .onClassLoaderOf(testClass)
                .withPath(path)
                .bytes();
    }
}
