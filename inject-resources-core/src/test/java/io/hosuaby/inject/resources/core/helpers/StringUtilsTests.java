package io.hosuaby.inject.resources.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import io.hosuaby.inject.resources.core.helpers.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class StringUtilsTests {

    @Test
    @DisplayName("Test ucfirst")
    public void testUcfirst() {

        /* Given */
        String str = "parameter";

        /* When */
        String ucfirst = StringUtils.ucfirst(str);

        /* Then */
        assertThat(ucfirst)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("Parameter");
    }

    @DisplayName("Test isNotBlank")
    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource(value = {
            "'', false",
            "'   ', false",
            "'blank ', true",
            "' not blank   ', true"})
    public void testIsNotBlank(String str, boolean isNotBlank) {
        assertThat(StringUtils.isNotBlank(str))
                .isEqualTo(isNotBlank);
    }

    @DisplayName("Test isBlank")
    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource(value = {
            "'', true",
            "'   ', true",
            "'blank ', false",
            "' not blank   ', false"})
    public void testIsBlank(String str, boolean isBlank) {
        assertThat(StringUtils.isBlank(str))
                .isEqualTo(isBlank);
    }

    @DisplayName("Test blankToNull")
    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource(nullValues = "null", value = {
            "'', null",
            "'   ', null",
            "'blank ', 'blank '",
            "' not blank   ', ' not blank   '"})
    public void testBlankToNull(String input, String output) {
        assertThat(StringUtils.blankToNull(input))
                .isEqualTo(output);
    }
}
