package com.adelean.inject.resources.junit.jupiter.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.adelean.inject.resources.junit.jupiter.core.helpers.OptionalUtils;

public class OptionalUtilsTests {

    @Test
    @DisplayName("Test first present when one is present")
    public void testFirstPresent_oneIsPresent() {

        /* Given */
        // NOTHING

        /* When */
        Integer firstPresent = OptionalUtils
                .firstPresent(
                        Optional::empty,
                        Optional::empty,
                        () -> Optional.of(42),
                        Optional::empty,
                        () -> Optional.of(7)
                ).orElse(13);

        /* Then */
        assertThat(firstPresent)
                .isEqualTo(42);
    }

    @Test
    @DisplayName("Test first present when none is present")
    public void testFirstPresent_noneIsPresent() {

        /* Given */
        // NOTHING

        /* When */
        var firstPresent = OptionalUtils
                .firstPresent(
                        Optional::empty,
                        Optional::empty,
                        Optional::empty
                ).orElse(13);

        /* Then */
        assertThat(firstPresent)
                .isEqualTo(13);
    }
}
