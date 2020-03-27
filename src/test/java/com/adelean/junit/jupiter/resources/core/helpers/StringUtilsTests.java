package com.adelean.junit.jupiter.resources.core.helpers;

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
}
