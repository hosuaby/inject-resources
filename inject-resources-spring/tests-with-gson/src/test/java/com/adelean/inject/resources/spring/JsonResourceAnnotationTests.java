package com.adelean.inject.resources.spring;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import assertj.json.gson.JsonAssertions;
import com.adelean.inject.resources.spring.beans.BeanWithJsonResources;
import com.adelean.inject.resources.spring.beans.TestConfig;
import com.adelean.resources.data.Person;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("@JsonResource")
public class JsonResourceAnnotationTests {

    @Autowired
    BeanWithJsonResources beanWithJsonResources;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithJsonResources)
                .isNotNull();
    }

    @Test
    @DisplayName("injects JSON content into Map class field")
    public void testInjectJsonIntoMapClassField() {
        assertThat(BeanWithJsonResources.getJsonAsMap())
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
    public void testInjectJsonIntoJsonElementInstanceField() {
        var jsonElement = beanWithJsonResources.getJsonElement();

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
    @DisplayName("injects JSON content into typed constructor argument")
    public void testInjectJsonIntoTypedConstructorArgument() {
        assertThat(beanWithJsonResources.getSpongeBob())
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
    @DisplayName("injects JSON array into List constructor argument")
    public void testInjectJsonArrayIntoListConstructorArgument() {
        assertThat(beanWithJsonResources.getVelibAsList())
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
    @DisplayName("injects JSON array into Map[] setter argument")
    public void testInjectJsonArrayIntoArraySetterArgument() {
        assertThat(beanWithJsonResources.getVelibAsArray())
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
}
