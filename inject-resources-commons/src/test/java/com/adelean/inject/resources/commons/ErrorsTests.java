package com.adelean.inject.resources.commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;

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

    @Test
    @DisplayName("Test how list of types is printed")
    public void testTypesToString() {

        /* Given */
        Class<?>[] types = { String.class, File.class, Path.class };
        String expected =
                "\t- java.lang.String\n"
                + "\t- java.io.File\n"
                + "\t- java.nio.file.Path";

        /* When */
        String str = Errors.typesToString(types);

        /* Then */
        assertThat(str)
                .isEqualTo(expected);
    }
}
