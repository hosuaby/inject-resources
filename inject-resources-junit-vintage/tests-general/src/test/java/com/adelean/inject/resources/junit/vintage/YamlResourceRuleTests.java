package com.adelean.inject.resources.junit.vintage;

import com.adelean.inject.resources.junit.vintage.yaml.YamlResource;
import com.adelean.resources.data.Person;
import org.junit.Rule;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class YamlResourceRuleTests {
    private static Yaml yaml = new Yaml();

    @Rule
    public YamlResource<Map<String, Object>> receipt = givenResource()
            .yaml("/com/adelean/junit/jupiter/receipt.yml")
            .parseWith(yaml);

    @Rule
    public YamlResource<Person> spongeBob = givenResource()
            .yaml("/com/adelean/junit/jupiter/sponge-bob.yaml")
            .parseWith(yaml);

    @Test
    @SuppressWarnings("unchecked")
    public void testLoadYamlIntoMap() {
        Map<String, Object> actual = receipt.get();
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4)
                .containsKeys("receipt", "date", "customer", "items")
                .containsEntry("receipt", "Oz-Ware Purchase Invoice")
                .containsEntry("date", Date.from(LocalDate
                        .of(2012, 8, 6)
                        .atStartOfDay()
                        .atZone(ZoneId.of("UTC"))
                        .toInstant()));
        assertThat((Map<String, String>) actual.get("customer"))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsEntry("first_name", "Dorothy")
                .containsEntry("family_name", "Gale");
        assertThat(actual.get("items"))
                .isNotNull()
                .asList()
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(
                        Map.of(
                                "part_no", "A4786",
                                "descrip", "Water Bucket (Filled)",
                                "price", 1.47,
                                "quantity", 4),
                        Map.of(
                                "part_no", "E1628",
                                "descrip", "High Heeled \"Ruby\" Slippers",
                                "size", 8,
                                "price", 133.7,
                                "quantity", 1));
    }

    @Test
    public void testLoadYamlIntoTypedObject() {
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
}
