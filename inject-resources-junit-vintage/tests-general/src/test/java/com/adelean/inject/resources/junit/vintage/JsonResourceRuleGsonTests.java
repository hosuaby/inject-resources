package com.adelean.inject.resources.junit.vintage;

import assertj.json.gson.JsonAssertions;
import com.adelean.inject.resources.junit.vintage.json.JsonResource;
import com.adelean.resources.data.Person;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class JsonResourceRuleGsonTests {
    private static final Gson gson = new Gson();

    @Rule
    public JsonResource<Map<String, Object>> jsonAsMap = givenResource()
            .json("/com/adelean/junit/jupiter/sponge-bob.json")
            .parseWith(gson);

    @Rule
    public JsonResource<JsonElement> jsonElement = givenResource()
            .json("/com/adelean/junit/jupiter/sponge-bob.json")
            .parseWith(gson);

    @Rule
    public JsonResource<Person> spongeBob = givenResource()
            .json("/com/adelean/junit/jupiter/sponge-bob.json")
            .parseWith(gson);

    @Rule
    public JsonResource<List<Map<String, ?>>> velibAsList = givenResource()
            .json("/com/adelean/junit/jupiter/velib.json")
            .parseWith(gson);


    @Rule
    public JsonResource<Map<String, ?>[]> velibAsArray = givenResource()
            .json("/com/adelean/junit/jupiter/velib.json")
            .parseWith(gson);

    @Test
    public void testLoadJsonIntoMap() {
        assertThat(jsonAsMap.get())
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
    public void testLoadJsonIntoJsonElement() {
        JsonElement actual = jsonElement.get();
        JsonAssertions
                .assertThat(actual)
                .isNotNull();
        JsonAssertions
                .assertThat(actual)
                .hasField("firstName")
                .asString()
                .isEqualTo("Bob");
        JsonAssertions
                .assertThat(actual)
                .hasField("lastName")
                .asString()
                .isEqualTo("Square Pants");
        JsonAssertions
                .assertThat(actual)
                .hasField("email")
                .asString()
                .isEqualTo("sponge.bob@bikinibottom.io");
        JsonAssertions
                .assertThat(actual)
                .hasField("age")
                .asInt()
                .isEqualTo(22);
        JsonAssertions
                .assertThat(actual)
                .hasPath("address", "address1")
                .asString()
                .isEqualTo("ananas house");
        JsonAssertions
                .assertThat(actual)
                .hasPath("address", "city")
                .asString()
                .isEqualTo("Bikini Bottom");
        JsonAssertions
                .assertThat(actual)
                .hasPath("address", "zipcode")
                .asInt()
                .isEqualTo(10101);
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
                                "capacity", 30.0),
                        ImmutableMap.of(
                                "name", "Charles Frérot - Albert Guilpin",
                                "nom_arrondissement_communes", "Gentilly",
                                "capacity", 23.0));
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
                                "capacity", 30.0),
                        ImmutableMap.of(
                                "name", "Charles Frérot - Albert Guilpin",
                                "nom_arrondissement_communes", "Gentilly",
                                "capacity", 23.0));
    }
}
