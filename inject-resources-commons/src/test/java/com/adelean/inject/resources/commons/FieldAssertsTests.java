package com.adelean.inject.resources.commons;

import com.adelean.inject.resources.annotations.SupportedTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static com.adelean.inject.resources.commons.FieldAsserts.assertArrayOrCollection;
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
                .isInstanceOf(RuntimeException.class)
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
                + " Supported types are:"
                + "\n\t- java.lang.String"
                + "\n\t- java.util.Properties";

        /* When */
        ThrowingCallable callable = () -> FieldAsserts
                .assertSupportedType(secondField, WithListOfSupportedTypes.class);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(expectedError);
    }

    @Test
    @DisplayName("Test assertSupportedType with parameters")
    public void testAssertSupportedType_withParameters() throws Exception {

        /* Given */
        var parameterWithSupportedType = TestClass.class.
                getDeclaredMethod("setFirstField", String.class)
                .getParameters()[0];
        var parameterAllTypesSupported = TestClass.class
                .getDeclaredMethod("setSecondField", Integer.class)
                .getParameters()[0];
        var parameterWithUnsupportedType = TestClass.class
                .getDeclaredMethod("setThirdField", Double.class)
                .getParameters()[0];

        /* When */
        ThrowingCallable assertParameterWithSupportedType = () -> FieldAsserts
                .assertSupportedType(parameterWithSupportedType, WithListOfSupportedTypes.class);
        ThrowingCallable assertParameterAllTypesSupported = () -> FieldAsserts
                .assertSupportedType(parameterAllTypesSupported, WithoutListOfSupportedTypes.class);
        ThrowingCallable assertParameterWithUnsupportedType = () -> FieldAsserts
                .assertSupportedType(parameterWithUnsupportedType, WithListOfSupportedTypes.class);

        /* Then */
        assertThatCode(assertParameterWithSupportedType)
                .doesNotThrowAnyException();
        assertThatCode(assertParameterAllTypesSupported)
                .doesNotThrowAnyException();
        assertThatCode(assertParameterWithUnsupportedType)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@WithListOfSupportedTypes cannot be resolved on parameter of type java.lang.Double. "
                        + "Supported types are:"
                        + "\n\t- java.lang.String"
                        + "\n\t- java.util.Properties");
    }

    @Test
    @DisplayName("Test assert array or collection")
    public void testAssertArrayOrCollection() {
        assertThatCode(() -> assertArrayOrCollection("field", byte[].class, GivenJsonLinesResource.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> assertArrayOrCollection("field", ArrayList.class, GivenJsonLinesResource.class))
                .doesNotThrowAnyException();
        assertThatCode(() -> assertArrayOrCollection("field", HashMap.class, GivenJsonLinesResource.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@GivenJsonLinesResource cannot be resolved on field of type java.util.HashMap. "
                    + "Field must be array or collection.");
    }

    private static class TestClass {
        public String firstField;
        protected Integer secondField;
        Double thirdField;
        private Boolean fourthField = null;

        void setFirstField(
                @WithListOfSupportedTypes
                String firstField) {
            this.firstField = firstField;
        }

        void setSecondField(
                @WithoutListOfSupportedTypes
                Integer secondField) {
            this.secondField = secondField;
        }

        void setThirdField(
                @WithListOfSupportedTypes
                Double thirdField) {
            this.thirdField = thirdField;
        }
    }

    private @interface WithoutListOfSupportedTypes {
    }

    @SupportedTypes({ String.class, Properties.class })
    private @interface WithListOfSupportedTypes {
    }

    private @interface GivenTextResource {
        String value() default "";
        String from() default "";
        String charset() default "UTF-8";
    }

    private @interface GivenJsonLinesResource {
        String value() default "";
        String from() default "";
        String charset() default "UTF-8";
        String jacksonMapper() default "";
        String gson() default "";
    }
}
