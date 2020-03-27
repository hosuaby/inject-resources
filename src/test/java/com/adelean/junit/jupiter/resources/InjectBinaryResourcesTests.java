package com.adelean.junit.jupiter.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@TestWithResources
@DisplayName("@GivenBinaryResource")
public class InjectBinaryResourcesTests {

    @GivenBinaryResource("/com/adelean/junit/jupiter/fibonacci.bin")
    byte[] fibonacci;

    @Test
    @DisplayName("injects binary content into byte[] field")
    public void testInjectTextIntoStringClassField() {
        assertThat(fibonacci)
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }
}
