package com.adelean.junit.jupiter.resources.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ResourceLoaderTests {

    @Test
    @DisplayName("Test read resource as text")
    public void testReadAsText() {

        /* Given */
        String resourcePath = "/com/adelean/junit/jupiter/resource.txt";
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
}
