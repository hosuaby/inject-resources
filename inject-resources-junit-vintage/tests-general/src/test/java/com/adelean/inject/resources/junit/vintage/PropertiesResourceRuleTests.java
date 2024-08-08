package com.adelean.inject.resources.junit.vintage;

import com.adelean.inject.resources.junit.vintage.properties.PropertiesResource;
import org.junit.Rule;
import org.junit.Test;

import static com.adelean.inject.resources.junit.vintage.GivenResource.givenResource;
import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesResourceRuleTests {

    @Rule
    public PropertiesResource dbProperties = givenResource()
            .properties("/com/adelean/junit/jupiter/db.properties");

    @Test
    public void testLoadProperties() {
        assertThat(dbProperties.get())
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsEntry("db.user", "hosuaby")
                .containsEntry("db.password", "password")
                .containsEntry("db.url", "localhost");
    }
}
