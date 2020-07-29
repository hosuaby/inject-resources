package com.adelean.inject.resources.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Test isNotBlank")
    public void testIsNotBlank() {

        /* Given */
        String str1 = "";
        String str2 = "   ";
        String str3 = "blank";
        String str4 = " not blank  ";

        /* Then */
        assertThat(StringUtils.isNotBlank(str1))
                .isFalse();
        assertThat(StringUtils.isNotBlank(str2))
                .isFalse();
        assertThat(StringUtils.isNotBlank(str3))
                .isTrue();
        assertThat(StringUtils.isNotBlank(str4))
                .isTrue();
    }

    @Test
    @DisplayName("Test isBlank")
    public void testIsBlank() {

        /* Given */
        String str1 = "";
        String str2 = "   ";
        String str3 = "blank";
        String str4 = " not blank  ";

        /* Then */
        assertThat(StringUtils.isBlank(str1))
                .isTrue();
        assertThat(StringUtils.isBlank(str2))
                .isTrue();
        assertThat(StringUtils.isBlank(str3))
                .isFalse();
        assertThat(StringUtils.isBlank(str4))
                .isFalse();
    }

    @Test
    @DisplayName("Test blankToNull")
    public void testBlankToNull() {

        /* Given */
        String str1 = "";
        String str2 = "   ";
        String str3 = "blank";
        String str4 = " not blank  ";

        /* Then */
        assertThat(StringUtils.blankToNull(str1))
                .isNull();
        assertThat(StringUtils.blankToNull(str2))
                .isNull();
        assertThat(StringUtils.blankToNull(str3))
                .isNotNull()
                .isNotBlank()
                .isEqualTo(str3);
        assertThat(StringUtils.blankToNull(str4))
                .isNotNull()
                .isNotBlank()
                .isEqualTo(str4);
    }
}
