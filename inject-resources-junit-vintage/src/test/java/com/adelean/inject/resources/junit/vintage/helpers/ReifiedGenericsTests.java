package com.adelean.inject.resources.junit.vintage.helpers;

import com.adelean.inject.resources.junit.vintage.BinaryResourceRuleTests;
import com.adelean.inject.resources.junit.vintage.GivenResource;
import com.adelean.inject.resources.junit.vintage.JsonLinesResourceRuleJacksonTests;
import com.adelean.inject.resources.junit.vintage.TextResourceRuleTests;
import com.adelean.inject.resources.junit.vintage.YamlResourceRuleTests;
import com.adelean.junit.jupiter.resources.data.jackson.Log;
import io.leangen.geantyref.TypeToken;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ReifiedGenericsTests {

    @Before
    public void purgeTestInstances() {
        ReifiedGenerics.testInstances.clear();
    }

    @Test
    public void testTargetType() {

        /* Given */
        var textResourceRuleTests = new TextResourceRuleTests();
        var yamlResourceRuleTests = new YamlResourceRuleTests();
        var jsonLinesResourceRuleJacksonTests = new JsonLinesResourceRuleJacksonTests();

        /* When */
        var binaryTargetType = ReifiedGenerics.targetType(
                BinaryResourceRuleTests.class, BinaryResourceRuleTests.fibonacciStatic);
        var textTargetType = ReifiedGenerics.targetType(
                TextResourceRuleTests.class, textResourceRuleTests.textResource);
        var yamlTargetType = ReifiedGenerics.targetType(
                YamlResourceRuleTests.class, yamlResourceRuleTests.receipt);
        var jsonLinesTargetType = ReifiedGenerics.targetType(
                JsonLinesResourceRuleJacksonTests.class, jsonLinesResourceRuleJacksonTests.logsAsArray);

        /* Then */
        assertThat(binaryTargetType)
                .isNotNull()
                .isEqualTo(byte[].class);
        assertThat(textTargetType)
                .isNotNull()
                .isEqualTo(String.class);
        assertThat(yamlTargetType)
                .isNotNull()
                .isEqualTo(new TypeToken<Map<String, Object>>() {}.getType());
        assertThat(jsonLinesTargetType)
                .isNotNull()
                .isEqualTo(Log[].class);
    }

    @Test
    public void testTargetType_missingRule() {

        /* Given */
        var textResourceRule = GivenResource
                .givenResource()
                .text("path/to/some/resource.txt");

        /* When */
        ThrowableAssert.ThrowingCallable getTargetType = () ->
                ReifiedGenerics.targetType(TextResourceRuleTests.class, textResourceRule);

        /* Then */
        assertThatCode(getTargetType)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cannot find matching resource rule.");
    }

    @Test
    public void testTestInstance() {

        /* Given */
        assertThat(ReifiedGenerics.testInstances)
                .isNotNull()
                .isEmpty();

        /* When */
        var firstInstance = ReifiedGenerics.testInstance(ClassWithDefaultConstructor.class);

        /* Then */
        assertThat(firstInstance)
                .isNotNull();
        assertThat(ReifiedGenerics.testInstances)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .containsEntry(ClassWithDefaultConstructor.class, firstInstance);

        /* When */
        var secondInstance = ReifiedGenerics.testInstance(ClassWithDefaultConstructor.class);

        /* Then */
        assertThat(secondInstance)
                .isNotNull()
                .isEqualTo(firstInstance);
        assertThat(ReifiedGenerics.testInstances)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .containsEntry(ClassWithDefaultConstructor.class, firstInstance);
    }

    @Test
    public void testNewInstance_classWithDefaultConstructor() {

        /* Given */
        // NOTHING

        /* When */
        var instance = ReifiedGenerics.newInstance(ClassWithDefaultConstructor.class);

        /* Then */
        assertThat(instance)
                .isNotNull()
                .isInstanceOf(ClassWithDefaultConstructor.class)
                .hasFieldOrPropertyWithValue("str", "La vie est belle!");
    }

    @Test
    public void testNewInstance_classWithoutDefaultConstructor() {
        assertThatCode(() -> ReifiedGenerics.newInstance(TestClass.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(String.format(
                        "Test class %s should have exactly one public zero-argument constructor.",
                        TestClass.class.getName()));
    }

    @Test
    public void testFieldValue() throws NoSuchFieldException {

        /* Given */
        var testInstance = new TestClass("La vie est belle!");
        var field = TestClass.class.getField("str");

        /* When */
        Object fieldValue = ReifiedGenerics.fieldValue(field, testInstance);

        /* Then */
        assertThat(fieldValue)
                .isNotNull()
                .isInstanceOf(String.class)
                .asString()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("La vie est belle!");
    }

    static class ClassWithDefaultConstructor {
        String str;

        public ClassWithDefaultConstructor() {
            this.str = "La vie est belle!";
        }
    }

    static class TestClass {
        public String str;

        public TestClass(String str) {
            this.str = str;
        }
    }
}
