package com.adelean.inject.resources.spring;

import com.adelean.inject.resources.spring.beans.BeanWithYamlResources;
import com.adelean.inject.resources.spring.beans.TestConfig;
import com.adelean.resources.data.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@DisplayName("@YamlResource")
public class YamlResourceAnnotationTests {

    @Autowired
    private BeanWithYamlResources beanWithYamlResources;

    @BeforeEach
    public void assertBeanInjected() {
        assertThat(beanWithYamlResources)
                .isNotNull();
    }

    @Test
    @DisplayName("injects content of YAML resource file into Map instance field")
    @SuppressWarnings("unchecked")
    public void testInjectsYamlIntoMapField() {
        var receipt = beanWithYamlResources.getReceipt();
        assertThat(receipt)
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
        assertThat((Map<String, String>) receipt.get("customer"))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsEntry("first_name", "Dorothy")
                .containsEntry("family_name", "Gale");
        assertThat(receipt.get("items"))
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
    @DisplayName("injects YAML content into typed instance field")
    public void testInjectsYamlIntoTypedField() {
        assertThat(beanWithYamlResources.getSpongeBob())
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
