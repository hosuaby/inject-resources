package com.adelean.inject.resources.junit.jupiter.core;

import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withParametersCount;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.platform.commons.support.ModifierSupport;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import com.adelean.inject.resources.junit.jupiter.TestsAdvice;
import com.adelean.inject.resources.junit.jupiter.core.helpers.Errors;

public final class TestsAdviceProcessor {
    private static final String ERR_MULTIPLE_TESTS_ADVICES_ON_CLASSPATH =
            "Multiple classes annotated with @TestsAdvice was found on classpath. Only one advice is allowed. "
            + "Following advice classes found:\n%s";
    private static final String MSG_TESTS_ADVICE_SPEC = "Tests advice class must be public, final, not extends any "
            + "other class, and have a public constructor without arguments.";
    private static final String ERR_TESTS_ADVICE_IS_NOT_PUBLIC = "Class %s annotated with @TestsAdvice is not public! "
            + MSG_TESTS_ADVICE_SPEC;
    private static final String ERR_TESTS_ADVICE_IS_NOT_FINAL = "Class %s annotated with @TestsAdvice is not final! "
            + MSG_TESTS_ADVICE_SPEC;
    private static final String ERR_TESTS_ADVICE_HAS_SUPERCLASS = "Class %s annotated with @TestsAdvice inherits from "
            + "class %s! " + MSG_TESTS_ADVICE_SPEC;
    private static final String ERR_TESTS_ADVICE_INVALID_CONSTRUCTOR = "Class %s annotated with @TestsAdvice doesn't "
            + "have a public constructor without arguments! " + MSG_TESTS_ADVICE_SPEC;

    public static Optional<Class<?>> findAdviceClass() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath()));

        Set<Class<?>> advices = reflections.getTypesAnnotatedWith(TestsAdvice.class);

        if (advices.isEmpty()) {
            return Optional.empty();
        }

        if (advices.size() > 1) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_MULTIPLE_TESTS_ADVICES_ON_CLASSPATH, Errors.typesToString(advices.toArray(new Class<?>[]{}))));
        }

        Class<?> testsAdvice = advices.iterator().next();
        assertTestAdviceClass(testsAdvice);
        return Optional.of(testsAdvice);
    }

    static void assertTestAdviceClass(Class<?> testsAdvice) {
        assertIsPublic(testsAdvice);
        assertIsFinal(testsAdvice);
        assertHasNoSuperclass(testsAdvice);
        assertHasDefaultConstructor(testsAdvice);
    }

    static void assertIsPublic(Class<?> testsAdvice) {
        if (!ModifierSupport.isPublic(testsAdvice)) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_TESTS_ADVICE_IS_NOT_PUBLIC, testsAdvice.getName()));
        }
    }

    static void assertIsFinal(Class<?> testsAdvice) {
        if (ModifierSupport.isNotFinal(testsAdvice)) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_TESTS_ADVICE_IS_NOT_FINAL, testsAdvice.getName()));
        }
    }

    static void assertHasNoSuperclass(Class<?> testsAdvice) {
        Class<?> superclass = testsAdvice.getSuperclass();
        if (superclass != Object.class) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_TESTS_ADVICE_HAS_SUPERCLASS, testsAdvice.getName(), superclass.getName()));
        }
    }

    static void assertHasDefaultConstructor(Class<?> testsAdvice) {
        @SuppressWarnings("rawtypes")
        Set<Constructor> validConstructor = getDefaultConstructor(testsAdvice);

        if (validConstructor.size() != 1) {
            throw new ExtensionConfigurationException(String.format(
                    ERR_TESTS_ADVICE_INVALID_CONSTRUCTOR, testsAdvice.getName()));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Set<Constructor> getDefaultConstructor(Class<?> testsAdvice) {
        return ReflectionUtils.getConstructors(testsAdvice, withModifier(Modifier.PUBLIC), withParametersCount(0));
    }
}
