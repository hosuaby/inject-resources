package com.adelean.inject.resources.junit.vintage.core;

import com.adelean.inject.resources.junit.vintage.helpers.CodeAnchor;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.adelean.inject.resources.core.Resource.buildPath;

/**
 * @author Alexei KLENIN
 */
public abstract class AbstractResourceRule<T> implements ResourceRule<T> {
    protected final CodeAnchor codeAnchor;
    protected final String path;
    protected volatile T content;

    protected AbstractResourceRule(CodeAnchor codeAnchor, String firstPathToken, String... otherTokens) {
        this.codeAnchor = codeAnchor;
        this.path = buildPath(firstPathToken, otherTokens);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        if (content == null) {
            content = load(base, description);
        }

        return base;
    }

    @Override
    public T get() {
        return content;
    }

    public boolean isSame(AbstractResourceRule<?> otherRule) {
        return this.codeAnchor.equals(otherRule.codeAnchor);
    }

    protected abstract T load(Statement base, Description description);
}
