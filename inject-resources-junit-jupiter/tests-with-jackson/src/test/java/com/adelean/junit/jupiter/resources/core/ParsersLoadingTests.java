package com.adelean.inject.resources.junit.jupiter.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.adelean.inject.resources.junit.jupiter.json.GsonProvider;
import com.adelean.inject.resources.junit.jupiter.json.JacksonMapperProvider;

public class ParsersLoadingTests {

    @Test
    @DisplayName("Test which parser provider classes are loadable")
    public void testIsLoadable() {
        assertThat(AbstractParserProvider.isLoadable(JacksonMapperProvider.class))
                .isTrue();
        assertThat(AbstractParserProvider.isLoadable(GsonProvider.class))
                .isFalse();
    }
}
