package com.adelean.junit.jupiter.resources.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ErrorsTests {

    @Test
    @DisplayName("Test template of internal error")
    public void testInternalError() {

        /* Given */
        String error = "<error>";

        /* When */
        String internalError = Errors.internalError(error);

        /* Then */
        assertThat(internalError)
                .isNotNull()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("INTERNAL ERROR: <error>. Please, open an issue on Github repository of project "
                        + "'junit-jupiter-resources'.");
    }
}
