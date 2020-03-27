package com.adelean.junit.jupiter.resources.core.helpers;

import com.adelean.junit.jupiter.resources.GivenTextResource;
import com.adelean.junit.jupiter.resources.core.annotations.SupportedTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Test FieldAsserts")
public class FieldAssertsTests {

    @Test
    @DisplayName("assertNonPrivate on public field do not throw any exception")
    public void testAssertNonPrivate_publicField() throws NoSuchFieldException {

        /* Given */
        Field publicField = TestClass.class.getDeclaredField("firstField");

        /* When */
        ThrowingCallable callable = () -> FieldAsserts
                .assertNonPrivate(publicField, GivenTextResource.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNonPrivate on protected field do not throw any exception")
    public void testAssertNonPrivate_protectedField() throws NoSuchFieldException {

        /* Given */
        Field protectedField = TestClass.class.getDeclaredField("secondField");

        /* When */
        ThrowingCallable callable = () ->
                FieldAsserts.assertNonPrivate(protectedField, GivenTextResource.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNonPrivate on package private field do not throw any exception")
    public void testAssertNonPrivate_packagePrivateField() throws NoSuchFieldException {

        /* Given */
        Field packagePrivateField = TestClass.class.getDeclaredField("thirdField");

        /* When */
        ThrowingCallable callable = () ->
                FieldAsserts.assertNonPrivate(packagePrivateField, GivenTextResource.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNonPrivate on private field throws an exception")
    public void testAssertNonPrivate_privateField() throws NoSuchFieldException {

        /* Given */
        Field privateField = TestClass.class.getDeclaredField("fourthField");

        /* When */
        ThrowingCallable callable = () -> FieldAsserts.assertNonPrivate(privateField, GivenTextResource.class);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(ExtensionConfigurationException.class)
                .hasMessage("@GivenTextResource field [fourthField] must not be private.");
    }

    @Test
    @DisplayName("assertSupportedType without list of types do not throw exception")
    public void testAssertSupportedType_allTypesSupported() throws Exception {

        /* Given */
        Field firstField = TestClass.class.getDeclaredField("firstField");

        /* When */
        ThrowingCallable callable = () -> FieldAsserts
                .assertSupportedType(firstField, WithoutListOfSupportedTypes.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertSupportedType when field type matches supported type do not throw exception")
    public void testAssertSupportedType_typeIsSupported() throws Exception {

        /* Given */
        Field firstField = TestClass.class.getDeclaredField("firstField");

        /* When */
        ThrowingCallable callable = () -> FieldAsserts
                .assertSupportedType(firstField, WithListOfSupportedTypes.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertSupportedType thrown an exception when field type is not supported")
    public void testAssertSupportedType_typeIsNotSupported() throws Exception {

        /* Given */
        Field secondField = TestClass.class.getDeclaredField("secondField");
        String expectedError =
                "@WithListOfSupportedTypes cannot be resolved on field of type java.lang.Integer."
                + " Supported types are:\n"
                + "\t- java.lang.String\n"
                + "\t- java.util.Properties\n";

        /* When */
        ThrowingCallable callable = () -> FieldAsserts
                .assertSupportedType(secondField, WithListOfSupportedTypes.class);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(ExtensionConfigurationException.class)
                .hasMessage(expectedError);
    }

    @Test
    @DisplayName("Test how list of types is printed")
    public void testTypesToString() {

        /* Given */
        Class<?>[] types = { String.class, File.class, Path.class };
        String expected =
                "\t- java.lang.String\n"
                + "\t- java.io.File\n"
                + "\t- java.nio.file.Path\n";

        /* When */
        String str = FieldAsserts.typesToString(types);

        /* Then */
        assertThat(str)
                .isEqualTo(expected);
    }

    private static class TestClass {
        public String firstField;
        protected Integer secondField;
        Double thirdField;
        private Boolean fourthField = null;
    }

    private @interface WithoutListOfSupportedTypes {
    }

    @SupportedTypes({ String.class, Properties.class })
    private @interface WithListOfSupportedTypes {
    }
}
