package com.adelean.inject.resources.junit.jupiter;

import com.adelean.inject.resources.junit.jupiter.core.helpers.ClasspathSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathSupportTests {

    @Test
    @DisplayName("Test that Jackson is absent")
    public void testJacksonIsAbsent() {
        var jackson2Present = ClasspathSupport.isJackson2Present();
        assertThat(jackson2Present)
                .isFalse();
    }

    @Test
    @DisplayName("Test that Gson is present")
    public void testGsonIsPresent() {
        var gsonPresent = ClasspathSupport.isGsonPresent();
        assertThat(gsonPresent)
                .isTrue();
    }
}
