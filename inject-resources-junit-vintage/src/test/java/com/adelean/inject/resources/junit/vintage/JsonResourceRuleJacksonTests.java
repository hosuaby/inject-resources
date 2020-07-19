package com.adelean.inject.resources.junit.vintage;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import com.adelean.inject.resources.junit.vintage.json.JsonResource;
import com.adelean.junit.jupiter.resources.data.Person;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class JsonResourceRuleJacksonTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ClassRule
    public static JsonResource<Map<String, Object>> jsonAsMap = givenResource()
            .json("/com/adelean/junit/jupiter/sponge-bob.json")
            .parseWith(objectMapper);

    @ClassRule
    public static JsonResource<JsonNode> jsonNode = givenResource()
            .json("/com/adelean/junit/jupiter/sponge-bob.json")
            .parseWith(objectMapper);

    @Rule
    public JsonResource<Person> spongeBob = givenResource()
            .json("/com/adelean/junit/jupiter/sponge-bob.json")
            .withCharset(StandardCharsets.UTF_8)
            .parseWith(objectMapper);

    @Rule
    public JsonResource<List<Map<String, ?>>> velibAsList = givenResource()
            .json("/com/adelean/junit/jupiter/velib.json")
            .parseWith(objectMapper)
            .withCharset(StandardCharsets.UTF_8);

    @Rule
    public JsonResource<Map<String, ?>[]> velibAsArray = givenResource()
            .json("/com/adelean/junit/jupiter/velib.json")
            .parseWith(objectMapper);

    @Test
    public void testLoadJsonIntoMap() {
        assertThat(jsonAsMap.get())
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
    public void testLoadJsonIntoJsonNode() {
        assertThat(jsonNode.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    public void testLoadJsonIntoTypedObject() {
        assertThat(spongeBob.get())
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
    public void testLoadJsonArrayIntoList() {
        assertThat(velibAsList.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        ImmutableMap.of(
                                "name", "Mairie du 12ème",
                                "nom_arrondissement_communes", "Paris",
                                "capacity", 30),
                        ImmutableMap.of(
                                "name", "Charles Frérot - Albert Guilpin",
                                "nom_arrondissement_communes", "Gentilly",
                                "capacity", 23));
    }

    @Test
    public void testLoadJsonArrayIntoArray() {
        assertThat(velibAsArray.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        ImmutableMap.of(
                                "name", "Mairie du 12ème",
                                "nom_arrondissement_communes", "Paris",
                                "capacity", 30),
                        ImmutableMap.of(
                                "name", "Charles Frérot - Albert Guilpin",
                                "nom_arrondissement_communes", "Gentilly",
                                "capacity", 23));
    }
}
