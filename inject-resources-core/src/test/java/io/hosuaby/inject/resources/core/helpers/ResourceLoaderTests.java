package io.hosuaby.inject.resources.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ResourceLoaderTests {

    @Test
    @DisplayName("Test read resource as text")
    public void testReadAsText() {

        /* Given */
        String resourcePath = "/io/hosuaby/resource.txt";
        ResourceLoader resourceLoader = new ResourceLoader(ResourceLoaderTests.class);

        /* When */
        String text = resourceLoader.readAsText(resourcePath, StandardCharsets.UTF_8);

        /* Then */
        assertThat(text)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("The quick brown fox jumps over the lazy dog.");
    }

    @Test
    @DisplayName("Test read resource as byte array")
    public void testReadAsByteArray() {

        /* Given */
        String resourcePath = "/io/hosuaby/fibonacci.bin";
        ResourceLoader resourceLoader = new ResourceLoader(ResourceLoaderTests.class);

        /* When */
        byte[] bytes = resourceLoader.readAsByteArray(resourcePath);

        /* Then */
        assertThat(bytes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(11)
                .contains(1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
    }

    @Test
    @DisplayName("Test try to load missing resource")
    public void testTryToLoadMissingResource() {

        /* Given */
        String resourcePath = "/io/hosuaby/missing.file";
        ResourceLoader resourceLoader = new ResourceLoader(ResourceLoaderTests.class);

        /* When */
        ThrowingCallable tryToLoadResource = () -> resourceLoader.resourceStream(resourcePath);

        /* Then */
        assertThatCode(tryToLoadResource)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Resource 'io/hosuaby/missing.file' is missing.");
    }
}
