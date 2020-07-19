package com.adelean.inject.resources.junit.vintage.json;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.adelean.inject.resources.junit.vintage.shared.DummyStatement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.assertj.core.api.ThrowableAssert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import java.nio.charset.StandardCharsets;

public class AbstractJsonResourceTests {

    @Test
    public void testValidJsonRule() {

        /* Given */
        var validExample = new TestClassWithValidJsonResourceRules();
        var dummyStatement = new DummyStatement();
        var testDescription = Description.createTestDescription(
                TestClassWithValidJsonResourceRules.class, "test-description");

        /* When */
        ThrowableAssert.ThrowingCallable applyJsonResourceRule = () ->
                TestClassWithValidJsonResourceRules.validJsonResource.apply(dummyStatement, testDescription);
        ThrowableAssert.ThrowingCallable applyJsonLinesResourceRule = () ->
                validExample.validJsonLinesResource.apply(dummyStatement, testDescription);

        /* Then */
        assertThatCode(applyJsonResourceRule)
                .doesNotThrowAnyException();
        assertThatCode(applyJsonLinesResourceRule)
                .doesNotThrowAnyException();
    }

    @Test
    public void testJsonRuleWithoutParser() {

        /* Given */
        var invalidExample = new TestClassWithJsonResourceRuleWithoutParser();
        var dummyStatement = new DummyStatement();
        var testDescription = Description.createTestDescription(
                TestClassWithJsonResourceRuleWithoutParser.class, "test-description");

        /* When */
        ThrowableAssert.ThrowingCallable applyJsonResourceRule = () ->
                invalidExample.jsonResourceWithoutParser.apply(dummyStatement, testDescription);

        /* Then */
        assertThatCode(applyJsonResourceRule)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Rule JsonResource must have defined parser.");
    }

    @Test
    public void testJsonRuleWithParserOfWrongType() {

        /* Given */
        var invalidExample = new TestClassWithJsonResourceRuleWithWrongParser();
        var dummyStatement = new DummyStatement();
        var testDescription = Description.createTestDescription(
                TestClassWithJsonResourceRuleWithWrongParser.class, "test-description");

        /* When */
        ThrowableAssert.ThrowingCallable applyJsonResourceRule = () ->
                invalidExample.jsonResourceWithoutParser.apply(dummyStatement, testDescription);

        /* Then */
        assertThatCode(applyJsonResourceRule)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong parser type: java.lang.Object.\nAccepted parser types:"
                    + "\n\t- com.fasterxml.jackson.databind.ObjectMapper"
                    + "\n\t- com.google.gson.Gson");
    }

    public static class TestClassWithValidJsonResourceRules {
        private static final ObjectMapper objectMapper = new ObjectMapper();
        private static final Gson gson = new Gson();

        @ClassRule
        public static JsonResource<Object> validJsonResource = givenResource()
                .json("/com/adelean/junit/jupiter/sponge-bob.json")
                .parseWith(objectMapper);

        @Rule
        public JsonLinesResource<Object[]> validJsonLinesResource = givenResource()
                .jsonLines("/com/adelean/junit/jupiter/logs.jsonl")
                .parseWith(gson)
                .withCharset(StandardCharsets.UTF_8);
    }

    public static class TestClassWithJsonResourceRuleWithoutParser {
        @Rule
        public JsonResource<Object> jsonResourceWithoutParser = givenResource()
                .json("/com/adelean/junit/jupiter/sponge-bob.json");
    }

    public static class TestClassWithJsonResourceRuleWithWrongParser {
        @Rule
        public JsonResource<Object> jsonResourceWithoutParser = givenResource()
                .json("/com/adelean/junit/jupiter/sponge-bob.json")
                .parseWith(new Object());
    }
}
