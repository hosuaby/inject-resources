package com.adelean.junit.jupiter.resources;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.adelean.inject.resources.commons.ClasspathSupport;

public class ClasspathSupportTests {

    @Test
    @DisplayName("Test that Jackson is present")
    public void testJacksonIsPresent() {
        var jackson2Present = ClasspathSupport.isJackson2Present();
        assertThat(jackson2Present)
                .isTrue();
    }

    @Test
    @DisplayName("Test that Gson is absent")
    public void testGsonIsAbsent() {
        var gsonPresent = ClasspathSupport.isGsonPresent();
        assertThat(gsonPresent)
                .isFalse();
    }
}
