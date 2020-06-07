package com.adelean.inject.resources.junit.jupiter.core;

import com.adelean.inject.resources.junit.jupiter.core.AbstractParserProvider;
import com.adelean.inject.resources.junit.jupiter.json.GsonProvider;
import com.adelean.inject.resources.junit.jupiter.json.JacksonMapperProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParsersLoadingTests {

    @Test
    @DisplayName("Test which parser provider classes are loadable")
    public void testIsLoadable() {
        assertThat(AbstractParserProvider.isLoadable(JacksonMapperProvider.class))
                .isFalse();
        assertThat(AbstractParserProvider.isLoadable(GsonProvider.class))
                .isTrue();
    }
}
