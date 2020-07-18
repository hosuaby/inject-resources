package com.adelean.inject.resources.junit.jupiter.json;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;

public class JacksonJsonLinesResourcesInjectorTests {

    @Test
    @DisplayName("Test assert array or collection")
    public void testAssertArrayOrCollection() {
        assertThatCode(() -> JacksonJsonLinesResourcesInjector.assertArrayOrCollection("field", byte[].class))
                .doesNotThrowAnyException();
        assertThatCode(() -> JacksonJsonLinesResourcesInjector.assertArrayOrCollection("field", ArrayList.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> JacksonJsonLinesResourcesInjector.assertArrayOrCollection("field", HashMap.class))
                .isInstanceOf(ExtensionConfigurationException.class)
                .hasMessage("@GivenJsonLinesResource cannot be resolved on field of type java.util.HashMap. "
                        + "Field must be array or collection.");
    }
}
