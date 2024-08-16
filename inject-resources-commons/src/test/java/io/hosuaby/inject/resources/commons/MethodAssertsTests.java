package io.hosuaby.inject.resources.commons;

import io.hosuaby.inject.resources.annotations.SupportedTypes;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Test MethodAsserts")
public class MethodAssertsTests {

    @Test
    @DisplayName("assertNonPrivate on public method do not throw any exception")
    public void testAssertNonPrivate_publicMethod() throws NoSuchMethodException {

        /* Given */
        Method publicMethod = TestClass.class.getDeclaredMethod("firstMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertNonPrivate(publicMethod, WithJacksonMapper.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNonPrivate on protected method do not throw any exception")
    public void testAssertNonPrivate_protectedMethod() throws NoSuchMethodException {

        /* Given */
        Method protectedMethod = TestClass.class.getDeclaredMethod("secondMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertNonPrivate(protectedMethod, WithJacksonMapper.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNonPrivate on package private method do not throw any exception")
    public void testAssertNonPrivate_packagePrivateMethod() throws NoSuchMethodException {

        /* Given */
        Method packagePrivateMethod = TestClass.class.getDeclaredMethod("thirdMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertNonPrivate(packagePrivateMethod, WithJacksonMapper.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNonPrivate on private method throws an exception")
    public void testAssertNonPrivate_privateMethod() throws NoSuchMethodException {

        /* Given */
        Method privateMethod = TestClass.class.getDeclaredMethod("fourthMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertNonPrivate(privateMethod, WithJacksonMapper.class);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@WithJacksonMapper method [fourthMethod] must not be private.");
    }

    @Test
    @DisplayName("assertNoArguments on method without arguments")
    public void testAssertNoArguments_methodWithoutArguments() throws NoSuchMethodException {

        /* Given */
        Method methodWithoutArguments = TestClass.class.getDeclaredMethod("firstMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertNoArguments(methodWithoutArguments, WithJacksonMapper.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertNoArguments on method with arguments")
    public void testAssertNoArguments_methodWithArguments() throws NoSuchMethodException {

        /* Given */
        Method methodWithArguments = TestClass.class.getDeclaredMethod("fifthMethod", Integer.class);

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertNoArguments(methodWithArguments, WithJacksonMapper.class);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@WithJacksonMapper method [fifthMethod] must have no arguments.");
    }

    @Test
    @DisplayName("assertReturnsSupportedType without list of types do not throw exception")
    public void testAssertReturnsSupportedType_allTypesSupported() throws NoSuchMethodException {

        /* Given */
        Method firstMethod = TestClass.class.getDeclaredMethod("firstMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertReturnsSupportedType(firstMethod, WithoutSupportedType.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertReturnsSupportedType when method returns supported type do not throw exception")
    public void testAssertReturnsSupportedType_typeIsSupported() throws NoSuchMethodException {

        /* Given */
        Method firstMethod = TestClass.class.getDeclaredMethod("firstMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertReturnsSupportedType(firstMethod, WithSupportedType.class);

        /* Then */
        assertThatCode(callable)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("assertReturnsSupportedType thrown an exception when method return type is not supported")
    public void testAssertReturnsSupportedType_typeIsNotSupported() throws NoSuchMethodException {

        /* Given */
        Method secondMethod = TestClass.class.getDeclaredMethod("secondMethod");

        /* When */
        ThrowingCallable callable = () -> MethodAsserts
                .assertReturnsSupportedType(secondMethod, WithSupportedType.class);

        /* Then */
        assertThatCode(callable)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@WithSupportedType method [secondMethod] returns unsupported type java.lang.Integer. "
                        + "Must be java.lang.String.");
    }

    @Test
    @DisplayName("Test assert not constructor")
    public void testAssertNotConstructor() throws Exception {

        /* Given */
        var notConstructor = TestClass.class.getDeclaredMethod("setFirstField", String.class);
        var constructor = TestClass.class.getDeclaredConstructor();

        /* When */
        ThrowingCallable assertNotConstructor = () -> MethodAsserts
                .assertNotConstructor(notConstructor, GivenTextResource.class);
        ThrowingCallable assertConstructor = () -> MethodAsserts
                .assertNotConstructor(constructor, GivenTextResource.class);

        /* Then */
        assertThatCode(assertNotConstructor)
                .doesNotThrowAnyException();
        assertThatCode(assertConstructor)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@GivenTextResource is not supported on constructor parameters. "
                        + "Please use field injection instead.");
    }

    @Test
    @DisplayName("Test assert not static method")
    public void testAssertNotStaticMethod() throws Exception {

        /* Given */
        var notStaticMethod = TestClass.class.getDeclaredMethod("setFirstField", String.class);
        var someStaticMethod = TestClass.class.getDeclaredMethod("someStaticMethod");

        /* When */
        ThrowingCallable assertNotStaticMethod = () -> MethodAsserts
                .assertNotStaticMethod(notStaticMethod, GivenTextResource.class);
        ThrowingCallable assertSomeStaticMethod =  () -> MethodAsserts
                .assertNotStaticMethod(someStaticMethod, GivenTextResource.class);

        /* Then */
        assertThatCode(assertNotStaticMethod)
                .doesNotThrowAnyException();
        assertThatCode(assertSomeStaticMethod)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("@GivenTextResource is not supported on parameters of static methods. "
                        + "Please use instance method parameter injection instead.");
    }

    private static class TestClass {
        static void someStaticMethod() {
        }

        void setFirstField(
                @WithSupportedType
                String firstField) {
        }

        void setSecondField(
                @WithoutSupportedType
                Integer secondField) {
        }

        void setThirdField(
                @WithSupportedType
                Double thirdField) {
        }

        public String firstMethod() {
            return "";
        }

        protected Integer secondMethod() {
            return 0;
        }

        Double thirdMethod() {
            return 0D;
        }

        private Boolean fourthMethod() {
            return false;
        }

        Integer fifthMethod(Integer i) {
            return i;
        }
    }

    private @interface WithoutSupportedType {
    }

    @SupportedTypes({ String.class })
    private @interface WithSupportedType {
    }

    private @interface WithJacksonMapper {
        String value() default "";
        String name() default "";
    }

    private @interface GivenTextResource {
    }
}
