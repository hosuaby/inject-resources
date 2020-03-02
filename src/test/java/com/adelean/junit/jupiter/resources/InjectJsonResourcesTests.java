package com.adelean.junit.jupiter.resources;

import static com.adelean.junit.jupiter.resources.json.JacksonJsonResourcesInjector.ERR_NAMED_JACKSON_MAPPER_MISSING;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

import java.util.Map;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

import com.adelean.junit.jupiter.resources.data.Job;
import com.adelean.junit.jupiter.resources.data.Person;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestResources
@DisplayName("@GivenJsonResource")
public class InjectJsonResourcesTests {

    @WithJacksonMapper
    ObjectMapper objectMapper = new ObjectMapper();

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    Map<String, Object> jsonAsMap;

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    JsonNode jsonNode;

    @GivenJsonResource("/com/adelean/junit/jupiter/sponge-bob.json")
    Person spongeBob;

    @Test
    @DisplayName("injects JSON content into Map instance field")
    public void testInjectJsonIntoMap() {
        assertThat(jsonAsMap)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .containsEntry("firstName", "Bob")
                .containsEntry("lastName", "Square Pants")
                .containsEntry("email", "sponge.bob@bikinibottom.io")
                .containsEntry("age", 22)
                .hasEntrySatisfying("address", address -> assertThat(address)
                        .isNotNull()
                        .asInstanceOf(InstanceOfAssertFactories.MAP)
                        .isNotEmpty()
                        .hasSize(3)
                        .containsEntry("address1", "ananas house")
                        .containsEntry("city", "Bikini Bottom")
                        .containsEntry("zipcode", 10101));
    }

    @Test
    @DisplayName("injects JSON content into JsonNode instance field")
    public void testInjectJsonIntoJsonNode() {
        assertThat(jsonNode)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    @DisplayName("injects JSON content into typed instance field")
    public void testInjectJsonIntoTypedField() {
        assertThat(spongeBob)
                .isNotNull()
                .hasFieldOrPropertyWithValue("firstName", "Bob")
                .hasFieldOrPropertyWithValue("lastName", "Square Pants")
                .hasFieldOrPropertyWithValue("email", "sponge.bob@bikinibottom.io")
                .hasFieldOrPropertyWithValue("age", 22)
                .extracting("address", as(type(Person.Address.class)))
                .isNotNull()
                .hasFieldOrPropertyWithValue("address1", "ananas house")
                .hasFieldOrPropertyWithValue("city", "Bikini Bottom")
                .hasFieldOrPropertyWithValue("zipcode", 10101);
    }

    @Test
    @DisplayName("fails to inject JSON content if required named ObjectMapper missing")
    public void testFailIfNamedJacksonMapperNotFound() {
        EngineTestKit
                .engine("junit-jupiter")
                .configurationParameter("junit.jupiter.conditions.deactivate", "org.junit.*DisabledCondition")
                .selectors(selectClass(WithoutNamedParser.class))
                .execute()
                .testEvents()
                .assertThatEvents()
                .haveExactly(1, finishedWithFailure(
                        instanceOf(RuntimeException.class),
                        message(String.format(
                                ERR_NAMED_JACKSON_MAPPER_MISSING, "named-parser", "named-parser")::equals)));
    }

    abstract class SuperClassWithParser {

        @WithJacksonMapper("named-parser")
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("with superclass containing named JacksonMapper")
    class WithSuperClassWithNamedParser extends SuperClassWithParser {

        @GivenJsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json", jacksonMapper = "named-parser")
        Person spongeBob;

        @Test
        @DisplayName("uses named parser from superclass")
        public void testUsesNamedParserFromSuperclass() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("without any defined parser")
    class WithoutDefinedParser {

        @GivenJsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json")
        Person spongeBob;

        @Test
        @DisplayName("uses ObjectMapper by default")
        public void testUsesDefaultObjectMapper() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }

    @Disabled("Run by EngineTestKit")
    @TestResources
    static class WithoutNamedParser {

        @GivenJsonResource(from = "/com/adelean/junit/jupiter/sponge-bob.json", jacksonMapper = "named-parser")
        Person spongeBob;

        @Test
        @DisplayName("uses named parser from superclass")
        public void testUsesNamedParserFromSuperclass() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }

    @TestResources
    static class WithStaticFieldDefinedParser {

        @WithJacksonMapper
        static ObjectMapper mapper = new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        @Test
        @DisplayName("uses ObjectMapper from static field")
        public void testUsesParserFromStaticField(
                @GivenJsonResource("/com/adelean/junit/jupiter/job.json") Job job) {
            assertThat(job)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("jobId", 12);
            assertThat(job.getSteps())
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
        }
    }

    @TestResources
    static class WithStaticMethodDefinedParser {

        @WithJacksonMapper
        static ObjectMapper mapper() {
            return new ObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        }

        @Test
        @DisplayName("uses ObjectMapper from static method")
        public void testUsesParserFromStaticMethod(
                @GivenJsonResource("/com/adelean/junit/jupiter/job.json") Job job) {
            assertThat(job)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("jobId", 12);
            assertThat(job.getSteps())
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
        }
    }

    @TestResources
    static class WithInstanceMethodDefinedParser {

        @WithJacksonMapper
        ObjectMapper mapper() {
            return new ObjectMapper()
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        }

        @Test
        @DisplayName("uses ObjectMapper from instance method")
        public void testUsesParserFromInstanceMethod(
                @GivenJsonResource("/com/adelean/junit/jupiter/job.json") Job job) {
            assertThat(job)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("jobId", 12);
            assertThat(job.getSteps())
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
        }
    }
}
