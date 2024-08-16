package io.hosuaby.inject.resources.junit.vintage.yaml;

import io.hosuaby.inject.resources.junit.vintage.shared.DummyStatement;
import org.assertj.core.api.ThrowableAssert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.yaml.snakeyaml.Yaml;

import static io.hosuaby.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThatCode;

public class AbstractYamlResourceTests {

    @Test
    public void testValidYamlRule() {

        /* Given */
        var validExample = new TestClassWithValidYamlResourceRules();
        var dummyStatement = new DummyStatement();
        var testDescription = Description.createTestDescription(
                TestClassWithValidYamlResourceRules.class, "test-description");

        /* When */
        ThrowableAssert.ThrowingCallable applyYamlResourceRule = () ->
                TestClassWithValidYamlResourceRules.receipt.apply(dummyStatement, testDescription);
        ThrowableAssert.ThrowingCallable applyYamlDocumentsResourceRule = () ->
                validExample.stacktraceAsArray.apply(dummyStatement, testDescription);

        /* Then */
        assertThatCode(applyYamlResourceRule)
                .doesNotThrowAnyException();
        assertThatCode(applyYamlDocumentsResourceRule)
                .doesNotThrowAnyException();
    }

    @Test
    public void testYamlRuleWithoutParser() {

        /* Given */
        var invalidExample = new TestClassWithYamlResourceRuleWithoutParser();
        var dummyStatement = new DummyStatement();
        var testDescription = Description.createTestDescription(
                TestClassWithYamlResourceRuleWithoutParser.class, "test-description");

        /* When */
        ThrowableAssert.ThrowingCallable applyJsonResourceRule = () ->
                invalidExample.receipt.apply(dummyStatement, testDescription);

        /* Then */
        assertThatCode(applyJsonResourceRule)
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Rule YamlResource must have defined Yaml object.");
    }

    public static class TestClassWithValidYamlResourceRules {
        private static final Yaml yaml = new Yaml();

        @ClassRule
        public static YamlResource<Object> receipt = givenResource()
                .yaml("/io/hosuaby/receipt.yml")
                .parseWith(yaml);

        @Rule
        public YamlDocumentsResource<Object[]> stacktraceAsArray = givenResource()
                .yamlDocuments("/io/hosuaby/stacktrace.yaml")
                .parseWith(yaml);
    }

    public static class TestClassWithYamlResourceRuleWithoutParser {
        @Rule
        public YamlResource<Object> receipt = givenResource()
                .yaml("/io/hosuaby/receipt.yml");
    }
}
