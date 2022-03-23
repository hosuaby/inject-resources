package com.adelean.inject.resources.junit.jupiter.core;

import java.lang.reflect.Member;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.ModifierSupport;
import com.adelean.inject.resources.junit.jupiter.core.cdi.InjectionContext;

public final class TestContext {
    private static final int SCAN_PACKAGE_DEPT = 3;

    private final Class<?> testClass;

    @Nullable
    private final Object testInstance;

    private final InjectionContext injectionContext;

    private TestContext(Class<?> testClass, @Nullable Object testInstance, ExtensionContext context) {
        this.testClass = testClass;
        this.testInstance = testInstance;
        this.injectionContext = new InjectionContext(context);
    }

    public static TestContext ofClass(Class<?> testClass, ExtensionContext context) {
        Objects.requireNonNull(testClass);
        return new TestContext(testClass, null, context);
    }

    public static TestContext ofInstance(Object testInstance, ExtensionContext context) {
        Objects.requireNonNull(testInstance);
        return new TestContext(testInstance.getClass(), testInstance, context);
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public String packageForAdviceScan() {
        final String[] tokens = testClass.getPackage().getName().split("\\.");
        return Stream
                .of(tokens)
                .limit(SCAN_PACKAGE_DEPT)
                .collect(Collectors.joining("."));
    }

    @Nullable
    public Object getTestInstance() {
        return testInstance;
    }

    public InjectionContext getInjectionContext() {
        return injectionContext;
    }

    public <T extends Member> Predicate<T> memberSelector() {
        return testInstance != null
                ? ModifierSupport::isNotStatic
                : ModifierSupport::isStatic;
    }
}
