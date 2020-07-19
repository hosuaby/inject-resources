package com.adelean.inject.resources.junit.vintage.helpers;

import static com.adelean.inject.resources.junit.vintage.helpers.Asserts.assertArrayOrCollection;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.adelean.inject.resources.junit.vintage.json.JsonLinesResource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class AssertsTests {

    @Test
    public void testAssertArrayOrCollection() {
        assertThatCode(() -> assertArrayOrCollection(byte[].class, JsonLinesResource.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> assertArrayOrCollection(ArrayList.class, JsonLinesResource.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> assertArrayOrCollection(HashMap.class, JsonLinesResource.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Type argument of rule JsonLinesResource must be array or collection, "
                        + "but was java.util.HashMap.");
    }
}
