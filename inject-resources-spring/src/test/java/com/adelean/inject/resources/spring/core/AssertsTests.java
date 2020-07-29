package com.adelean.inject.resources.spring.core;

import com.adelean.inject.resources.spring.JsonLinesResource;
import com.adelean.inject.resources.spring.TextResource;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.reflections8.ReflectionUtils.getAnnotations;

public class AssertsTests {

    @Test
    @DisplayName("Test assert array or collection")
    public void testAssertArrayOrCollection() {
        assertThatCode(() -> Asserts.assertArrayOrCollection("field", byte[].class, JsonLinesResource.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> Asserts.assertArrayOrCollection("field", ArrayList.class, JsonLinesResource.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> Asserts.assertArrayOrCollection("field", HashMap.class, JsonLinesResource.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Field annotated with @JsonLinesResource must be array or collection, "
                        + "but was java.util.HashMap.");
    }

    @Test
    @DisplayName("Test assert no other annotations")
    public void testAssertNoOtherAnnotations() throws Exception {

        /* Given */
        var validField = TestClass.class.getDeclaredField("validField");
        var invalidField = TestClass.class.getDeclaredField("invalidField");
        var invalidParameter = TestClass.class.getDeclaredConstructor(String.class).getParameters()[0];
        var invalidSetter = TestClass.class.getDeclaredMethod("invalidSetter", String.class);

        /* When */
        ThrowableAssert.ThrowingCallable assertValidField = () -> Asserts.assertNoOtherAnnotations(
                validField, resourceAnnotation(validField));
        ThrowableAssert.ThrowingCallable assertInvalidField = () -> Asserts.assertNoOtherAnnotations(
                invalidField, resourceAnnotation(invalidField));
        ThrowableAssert.ThrowingCallable assertInvalidParameter = () -> Asserts.assertNoOtherAnnotations(
                invalidParameter, resourceAnnotation(invalidParameter));
        ThrowableAssert.ThrowingCallable assertInvalidSetter = () -> Asserts.assertNoOtherAnnotations(
                invalidSetter, resourceAnnotation(invalidSetter));

        /* Then */
        assertThatCode(assertValidField)
                .doesNotThrowAnyException();
        assertThatCode(assertInvalidField)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Field 'invalidField' annotated with @TextResource has other invalid annotations:\n"
                        + "\t- @Autowired");
        assertThatCode(assertInvalidParameter)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Parameter 'arg0' annotated with @TextResource has other invalid annotations:\n"
                        + "\t- @Autowired");
        assertThatCode(assertInvalidSetter)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Method 'invalidSetter' annotated with @TextResource has other invalid annotations:\n"
                        + "\t- @Autowired");
    }

    @SuppressWarnings("unchecked")
    static Annotation resourceAnnotation(AnnotatedElement annotatedElement) {
        return getAnnotations(annotatedElement)
                .stream()
                .filter(annotation -> annotation instanceof TextResource)
                .findAny()
                .orElse(null);
    }

    static class TestClass {

        @TextResource("/com/adelean/junit/jupiter/resource.txt")
        String validField;

        @Autowired
        @TextResource("/com/adelean/junit/jupiter/resource.txt")
        String invalidField;

        TestClass(
                @Autowired
                @TextResource("/com/adelean/junit/jupiter/resource.txt")
                String invalidParameter) {
        }

        @Autowired
        @TextResource("/com/adelean/junit/jupiter/resource.txt")
        void invalidSetter(String ignored) {
        }
    }
}
