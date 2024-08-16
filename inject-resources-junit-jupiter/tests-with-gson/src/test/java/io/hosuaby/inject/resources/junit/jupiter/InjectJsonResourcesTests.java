package io.hosuaby.inject.resources.junit.jupiter;

import assertj.json.gson.JsonAssertions;
import io.hosuaby.inject.resources.junit.jupiter.GivenJsonResource;
import io.hosuaby.inject.resources.junit.jupiter.TestWithResources;
import io.hosuaby.inject.resources.junit.jupiter.WithGson;
import io.hosuaby.resources.data.Person;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

@TestWithResources
@DisplayName("@GivenJsonResource")
public class InjectJsonResourcesTests {

    @WithGson
    Gson gson = new Gson();

    @GivenJsonResource("/io/hosuaby/sponge-bob.json")
    Map<String, Object> jsonAsMap;

    @GivenJsonResource("/io/hosuaby/sponge-bob.json")
    JsonElement jsonElement;

    @GivenJsonResource("/io/hosuaby/sponge-bob.json")
    Person spongeBob;

    @GivenJsonResource("/io/hosuaby/velib.json")
    List<Map<String, ?>> velibAsList;

    @GivenJsonResource("/io/hosuaby/velib.json")
    Map<String, ?>[] velibAsArray;

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
                .containsEntry("age", 22.0d)
                .hasEntrySatisfying("address", address -> assertThat(address)
                        .isNotNull()
                        .asInstanceOf(InstanceOfAssertFactories.MAP)
                        .isNotEmpty()
                        .hasSize(3)
                        .containsEntry("address1", "ananas house")
                        .containsEntry("city", "Bikini Bottom")
                        .containsEntry("zipcode", 10101.0d));
    }

    @Test
    @DisplayName("injects JSON content into JsonElement instance field")
    public void testInjectJsonIntoJsonElement() {
        JsonAssertions
                .assertThat(jsonElement)
                .isNotNull();
        JsonAssertions
                .assertThat(jsonElement)
                .hasField("firstName")
                .asString()
                .isEqualTo("Bob");
        JsonAssertions
                .assertThat(jsonElement)
                .hasField("lastName")
                .asString()
                .isEqualTo("Square Pants");
        JsonAssertions
                .assertThat(jsonElement)
                .hasField("email")
                .asString()
                .isEqualTo("sponge.bob@bikinibottom.io");
        JsonAssertions
                .assertThat(jsonElement)
                .hasField("age")
                .asInt()
                .isEqualTo(22);
        JsonAssertions
                .assertThat(jsonElement)
                .hasPath("address", "address1")
                .asString()
                .isEqualTo("ananas house");
        JsonAssertions
                .assertThat(jsonElement)
                .hasPath("address", "city")
                .asString()
                .isEqualTo("Bikini Bottom");
        JsonAssertions
                .assertThat(jsonElement)
                .hasPath("address", "zipcode")
                .asInt()
                .isEqualTo(10101);
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
    @DisplayName("injects JSON array into List instance field")
    public void testInjectJsonArrayIntoListField() {
        assertThat(velibAsList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        Map.of(
                                "name", "Mairie du 12ème",
                                "nom_arrondissement_communes", "Paris",
                                "capacity", 30.0),
                        Map.of(
                                "name", "Charles Frérot - Albert Guilpin",
                                "nom_arrondissement_communes", "Gentilly",
                                "capacity", 23.0));
    }

    @Test
    @DisplayName("injects JSON array into Map[] instance field")
    public void testInjectJsonArrayIntoArrayField() {
        assertThat(velibAsArray)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        Map.of(
                                "name", "Mairie du 12ème",
                                "nom_arrondissement_communes", "Paris",
                                "capacity", 30.0),
                        Map.of(
                                "name", "Charles Frérot - Albert Guilpin",
                                "nom_arrondissement_communes", "Gentilly",
                                "capacity", 23.0));
    }

    @Test
    @DisplayName("fails to inject JSON content if required named Gson missing")
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
                                "Can't find named Gson '%s' in execution context. You probably forgot @WithGson(\"%s\").", "named-parser", "named-parser")::equals)));
    }

    abstract class SuperClassWithParser {

        @WithGson("named-parser")
        Gson gson = new Gson();
    }

    @Nested
    @DisplayName("with superclass containing named Gson")
    class WithSuperClassWithNamedParser extends SuperClassWithParser {

        @GivenJsonResource(from = "/io/hosuaby/sponge-bob.json", gson = "named-parser")
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

        @GivenJsonResource(from = "/io/hosuaby/sponge-bob.json")
        Person spongeBob;

        @Test
        @DisplayName("uses ObjectMapper by default")
        public void testUsesDefaultObjectMapper() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }

    @Disabled("Run by EngineTestKit")
    @TestWithResources
    static class WithoutNamedParser {

        @GivenJsonResource(from = "/io/hosuaby/sponge-bob.json", gson = "named-parser")
        Person spongeBob;

        @Test
        @DisplayName("uses named parser from superclass")
        public void testUsesNamedParserFromSuperclass() {
            assertThat(spongeBob)
                    .isNotNull();
        }
    }
}
