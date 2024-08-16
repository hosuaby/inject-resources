package io.hosuaby.junit.jupiter.resources;

import io.hosuaby.inject.resources.junit.jupiter.GivenTextResource;
import io.hosuaby.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@GivenTextResource")
public class InjectTextResourcesTests {

    @GivenTextResource("/io/hosuaby/resource.txt")
    static String classField;

    @GivenTextResource("/io/hosuaby/resource.txt")
    String instanceField;

    @Test
    @DisplayName("injects text content into String class field")
    public void testInjectTextIntoStringClassField() {
        assertThat(classField)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }

    @Test
    @DisplayName("injects text content into String instance field")
    public void testInjectTextIntoStringInstanceField() {
        assertThat(instanceField)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }

    @Test
    @DisplayName("injects text content into String method parameter")
    public void testInjectTextIntoStringMethodParameter(
            @GivenTextResource("/io/hosuaby/resource.txt")
            String methodParameter) {
        assertThat(methodParameter)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }
}
