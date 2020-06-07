package com.adelean.inject.resources.junit.jupiter.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import io.leangen.geantyref.TypeFactory;

public class JacksonJsonLinesResourcesInjectorTests {

    @Test
    @DisplayName("Test get element type")
    public void testElementType() {

        /* Given */
        var byteArrayType = TypeFactory.parameterizedClass(byte[].class);
        var stringArrayType = TypeFactory.parameterizedClass(String[].class);
        var arrayListType = TypeFactory.parameterizedClass(ArrayList.class, Long.class);
        var objectType = TypeFactory.parameterizedClass(Object.class);

        /* When */
        // NOTHING

        /* Then */
        assertThat(JacksonJsonLinesResourcesInjector.elementType(byteArrayType))
                .isNotNull()
                .isEqualTo(byte.class);
        assertThat(JacksonJsonLinesResourcesInjector.elementType(stringArrayType))
                .isNotNull()
                .isEqualTo(String.class);
        assertThat(JacksonJsonLinesResourcesInjector.elementType(arrayListType))
                .isNotNull()
                .isEqualTo(Long.class);
        assertThat(JacksonJsonLinesResourcesInjector.elementType(objectType))
                .isNull();
    }

    @Test
    @DisplayName("Test is array")
    public void testIsArray() {

        /* Given */
        var byteArrayType = TypeFactory.parameterizedClass(byte[].class);
        var stringArrayType = TypeFactory.parameterizedClass(String[].class);
        var arrayListType = TypeFactory.parameterizedClass(ArrayList.class, Object.class);
        var objectType = TypeFactory.parameterizedClass(Object.class);

        /* When */
        // NOTHING

        /* Then */
        assertThat(JacksonJsonLinesResourcesInjector.isArray(byteArrayType))
                .isTrue();
        assertThat(JacksonJsonLinesResourcesInjector.isArray(stringArrayType))
                .isTrue();
        assertThat(JacksonJsonLinesResourcesInjector.isArray(arrayListType))
                .isFalse();
        assertThat(JacksonJsonLinesResourcesInjector.isArray(objectType))
                .isFalse();
    }

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
