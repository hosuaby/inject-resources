package io.hosuaby.inject.resources.spring.core;

import io.hosuaby.inject.resources.spring.JsonLinesResource;
import io.hosuaby.inject.resources.spring.TextResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatCode;

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

    static class TestClass {

        @TextResource("/io/hosuaby/resource.txt")
        String validField;

        @Autowired
        @TextResource("/io/hosuaby/resource.txt")
        String invalidField;

        TestClass(
                @Autowired
                @TextResource("/io/hosuaby/resource.txt")
                String invalidParameter) {
        }

        @Autowired
        @TextResource("/io/hosuaby/resource.txt")
        void invalidSetter(String ignored) {
        }
    }
}
