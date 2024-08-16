package io.hosuaby.junit.jupiter.resources;

import io.hosuaby.inject.resources.junit.jupiter.GivenBinaryResource;
import io.hosuaby.inject.resources.junit.jupiter.TestWithResources;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@TestWithResources
@DisplayName("@GivenBinaryResource")
public class InjectBinaryResourcesTests {

    @GivenBinaryResource("/io/hosuaby/fibonacci.bin")
    byte[] fibonacci;

    @Test
    @DisplayName("injects binary content into byte[] field")
    public void testInjectBinaryContentIntoByteArrayInstanceField() {
        assertThat(fibonacci)
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }
}
